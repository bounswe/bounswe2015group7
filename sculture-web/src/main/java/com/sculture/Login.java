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


@WebServlet(name = "login")
public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpResponse<JsonNode> jsonResponse = null;
        try {


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", request.getParameter("form-username"));
            jsonObject.put("password", request.getParameter("form-password"));

            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/user/login")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
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
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

}
