package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.model.User;
import com.sculture.model.response.StoriesResponse;
import com.sculture.util.MyGson;
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
        String access_token = "";
        if(request.getSession().getAttribute("access_token") != null){
            access_token = request.getSession().getAttribute("access_token").toString();
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", user_id);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.USER_GET)
                    .header("Content-Type", "application/json")
                    .header("access-token", access_token)
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
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.USER_STORIES)
                    .header("Content-Type", "application/json")
                    .header("access-token", access_token)
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        StoriesResponse userStoriesResponse = new StoriesResponse();
        if (jsonResponse != null && !jsonResponse.getBody().getObject().has("exception")) {
            Object object = jsonResponse.getBody().getObject();
            Gson gson = MyGson.create();
            userStoriesResponse = gson.fromJson(object.toString(), StoriesResponse.class);
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
            request.setAttribute("errormsg", "Something went wrong getting this user's profile, please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
        request.setAttribute("relatedUser", user);
        request.setAttribute("user_stories", userStoriesResponse);
        request.getRequestDispatcher("/user_profile.jsp").forward(request, response);
    }

}

