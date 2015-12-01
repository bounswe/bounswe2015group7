package com.sculture;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "searchall")
public class SearchAll extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setAttribute("isLoggedIn", false);
//        request.setAttribute("username", "");
//        if (request.getSession().getAttribute("username") != null) {
//            request.setAttribute("username", request.getSession().getAttribute("username"));
//            request.setAttribute("isLoggedIn", true);
//        }
//        request.getRequestDispatcher("/add_story.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }


        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.post("http://52.28.216.93:9000/search/all")
                    .header("Content-Type", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            request.setAttribute("isLoggedIn", true);
            request.setAttribute("username", jsonResponse.getBody().getObject().get("username"));
            request.getSession().setAttribute("username", jsonResponse.getBody().getObject().get("username"));
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
        }


        request.getRequestDispatcher("/add_story.jsp").forward(request, response);
    }

}
