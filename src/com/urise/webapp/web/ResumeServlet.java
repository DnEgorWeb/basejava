package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html; charset=UTF-8");
        List<Resume> resumeList = storage.getAllSorted();
        PrintWriter writer = res.getWriter();
        writer.println("<table><tr><th>uuid</th><th>Full name</th></tr>");
        resumeList.forEach(r -> writer.println("<tr><td>" + r.getUuid() + "</td><td>" + r.getFullName() + "</td></tr>"));
        writer.println("</table>");
    }
}
