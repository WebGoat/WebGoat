package org.owasp.webgoat.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String userAgent = request.getHeader("user-agent");
        String clientBrowser = "Not known!";
        if (userAgent != null)
        {
            clientBrowser = userAgent;
        }
        request.setAttribute("client.browser", clientBrowser);
        request.getRequestDispatcher("/view.jsp").forward(request, response);
    }

}
