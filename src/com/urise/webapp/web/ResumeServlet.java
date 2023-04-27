package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.JsonParser;
import com.urise.webapp.web.validation.ResumeFormValidation;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    private Storage storage;
    private ResumeFormValidation formValidation;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
        formValidation = new ResumeFormValidation();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("resumes", storage.getAllSorted());
            req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, res);
            return;
        }
        Resume r;
        switch (action) {
            case "view":
                r = storage.get(uuid);
                ResumeForm form = ResumeForm.buildFrom(r);
                req.setAttribute("resume", form);
                req.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(req, res);
                return;
            case "create":
                ResumeForm emptyForm = ResumeForm.buildEmptyForm();
                req.setAttribute("resume", emptyForm);
                req.setAttribute("errors", emptyForm);
                req.setAttribute("action", "create");
                req.getRequestDispatcher("/WEB-INF/jsp/create-edit.jsp").forward(req, res);
                return;
            case "edit":
                r = storage.get(uuid);
                req.setAttribute("resume", ResumeForm.buildFrom(r));
                req.setAttribute("errors", ResumeForm.buildEmptyForm());
                req.setAttribute("action", "update");
                req.getRequestDispatcher("/WEB-INF/jsp/create-edit.jsp").forward(req, res);
                break;
            case "delete":
                storage.delete(uuid);
                res.sendRedirect("resume");
                return;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ResumeForm form = JsonParser.read(req.getParameter("resume"), ResumeForm.class);
        ResumeForm errors = formValidation.validate("resume", form);

        if (!errors.isEmpty()) {
            req.setAttribute("resume", form);
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/jsp/create-edit.jsp").forward(req, res);
            return;
        }

        String uuid = form.getUuid();
        Resume r = form.buildResume();
        if (uuid != null) {
            r.setUuid(uuid);
            storage.update(r);
        } else {
            storage.save(r);
        }
        res.sendRedirect("resume");
    }
}
