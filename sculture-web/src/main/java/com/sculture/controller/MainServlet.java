package com.sculture.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.model.response.StoriesResponse;
import com.sculture.util.MyGson;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bilal on 14/10/15.
 */
@WebServlet(name = "MainServlet")
public class MainServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //User login state
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }

        //Get popular stories using /search/all

        JSONObject params = new JSONObject();
        params.put("page", 1);
        params.put("size", 4);

        HttpResponse<JsonNode> jsonStoriesResponse = null;
        try {
            jsonStoriesResponse = Unirest.post("http://52.28.216.93:9000/search/all")
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(params.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        Gson gson = MyGson.create();

        StoriesResponse storiesResponse = gson.fromJson(jsonStoriesResponse.getBody().toString(), StoriesResponse.class);

        //Set the topStory attribute
        request.setAttribute("topStory", storiesResponse.getResult().get(3));

        //Set the popularStories
        request.setAttribute("popularStories", storiesResponse);
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);

    }

}
