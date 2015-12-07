package com.sculture;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bilal on 14/10/15.
 */
@WebServlet(name = "MainServlet")
public class MainServlet extends HttpServlet {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://52.28.216.93/user";
    static final String USER = "root";
    static final String PASS = "123456";
//    Connection conn = null;

//    @Override
//    public void init() throws ServletException {
//        super.init();
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }

        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

}
