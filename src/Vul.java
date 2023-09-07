package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;

public class Cls extends HttpServlet
{
    private static org.apache.log4j.Logger log = Logger.getLogger(Register.class);

    protected void danger(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");
        // ruleid:servletresponse-writer-xss
        resp.getWriter().write(input1);
    }

    protected void danger2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");
        PrintWriter writer = resp.getWriter();
        // ruleid:servletresponse-writer-xss
        writer.write(input1);
    }

    protected void ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");
        // ok:servletresponse-writer-xss
        resp.getWriter().write(Encode.forHtml(input1));
    }

    protected void ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input1 = req.getParameter("input1");
        String cleanParam = Jsoup.clean(input1, Whitelist.basic);
        // ok:servletresponse-writer-xss
        resp.getWriter().write(cleanParam);
    }
}
