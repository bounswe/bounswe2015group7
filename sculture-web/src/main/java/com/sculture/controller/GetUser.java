package com.sculture.controller;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.model.User;
import com.sculture.model.response.StoriesResponse;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Atakan Arıkan on 14.12.2015.
 */
@WebServlet(name = "getuser")
public class GetUser extends HttpServlet {

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
        User user = new User();
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }
        String requestURL = request.getRequestURL().toString();
        String user_id = requestURL.substring(requestURL.lastIndexOf('/') + 1);
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", user_id);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/user/get")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            Object object = jsonResponse.getBody().getObject();
            Gson gson = new Gson();
            user = gson.fromJson(object.toString(), User.class);
        }
        jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user_id);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/user/stories")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        StoriesResponse userStoriesResponse = new StoriesResponse();
        if (jsonResponse != null) {
            Object object = jsonResponse.getBody().getObject();
            Gson gson = new Gson();
            userStoriesResponse = gson.fromJson(object.toString(), StoriesResponse.class);
            System.out.println(object.toString());
        }
        request.setAttribute("relatedUser", user);
        request.setAttribute("user_stories", userStoriesResponse.getResult());
        request.getRequestDispatcher("/user_profile.jsp").forward(request, response);
    }

}

