package com.sculture;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by bilal on 14/10/15.
 */
@WebServlet(name = "signup")
public class Signup extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) System.out.println(enumeration.nextElement());
        try {


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", request.getParameter("form-email"));
            jsonObject.put("username", request.getParameter("form-username"));
            jsonObject.put("password", request.getParameter("form-password"));


            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://52.28.216.93:9000/user/register")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
            System.out.println(jsonResponse.getBody());
            System.out.println(request.getParameter("form-email"));
            System.out.println(request.getParameter("form-username"));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

}
