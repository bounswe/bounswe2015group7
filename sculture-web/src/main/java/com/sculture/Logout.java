package com.sculture;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by aarikan on 14/12/15.
 */
@WebServlet(name = "logout")
public class Logout extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        request.getSession().setAttribute("username", null);
        request.getSession().setAttribute("userid", null);
        request.getSession().setAttribute("access_token", null);
        request.setAttribute("isLoggedIn", false);
        response.sendRedirect("/index");
    }

}
