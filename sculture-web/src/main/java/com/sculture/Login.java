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
            jsonObject.put("email", request.getParameter("form-email"));
            jsonObject.put("password", request.getParameter("form-password"));
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.USER_LOGIN)
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null && !jsonResponse.getBody().getObject().has("exception")) {
            request.setAttribute("isLoggedIn", true);
            request.setAttribute("username", jsonResponse.getBody().getObject().get("username"));
            request.getSession().setAttribute("username", jsonResponse.getBody().getObject().get("username"));
            request.getSession().setAttribute("userid", jsonResponse.getBody().getObject().get("id"));
            request.getSession().setAttribute("access_token", jsonResponse.getBody().getObject().get("access_token").toString());
        } else {
            request.setAttribute("errormsg", "Something went wrong while logging you in, please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);

        }

        response.sendRedirect("/sculture/index");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

}
