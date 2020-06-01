package controllers;

import util.HibernateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * generic class that can be considered as a handler for welcome message or wrong requests
 */
@WebServlet(name = "hello", urlPatterns = "/")
public class HelloServlet extends HttpServlet {
    /**
     * default response for unknown requests
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HibernateUtil.getSessionFactory().openSession();
        resp.getWriter().write("Welcome Page");
    }
}