package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.ContactType;
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
            case "create":
                req.setAttribute("resume", ResumeForm.buildEmptyForm());
                req.setAttribute("errors", ResumeForm.buildEmptyForm());
                req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, res);
                return;
            case "delete":
                storage.delete(uuid);
                res.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        req.setAttribute("resume", r);
        req.getRequestDispatcher(("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"))
                .forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ResumeForm form = JsonParser.read(req.getParameter("resume"), ResumeForm.class);
        ResumeForm errors = formValidation.validate("resume", form);

        if (!errors.isEmpty()) {
            req.setAttribute("resume", form);
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, res);
            return;
        }

        Resume r = form.buildResume();
        storage.save(r);
        res.sendRedirect("resume");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uuid = req.getParameter("uuid");
        String fullName = req.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        storage.update(r);
        res.sendRedirect("resume");
    }
}
