package com.sculture;

import com.google.gson.Gson;
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

/**
 * Created by bilal on 14/10/15.
 */
@WebServlet(name = "sculture")
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


        //Get recommended stories
        StoriesResponse recommendedStoriesResponse = null;

        if((Boolean)request.getAttribute("isLoggedIn")) {
            JSONObject params = new JSONObject();
            params.put("page", 1);
            params.put("size", 4);

            HttpResponse<JsonNode> jsonStoriesResponse = null;
            try {
                jsonStoriesResponse = Unirest.post("http://52.59.252.52:9000/recommendation/similarToLiked")
                        .header("Content-Type", "application/json")
                        .header("access_token", (String)request.getSession().getAttribute("access_token"))
                        .body(new JsonNode(params.toString()))
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }


            Gson gson = MyGson.create();

            recommendedStoriesResponse = gson.fromJson(jsonStoriesResponse.getBody().toString(), StoriesResponse.class);
        }

        //Get popular stories using /search/all

        JSONObject params = new JSONObject();
        params.put("page", 1);
        params.put("size", 4);

        HttpResponse<JsonNode> jsonStoriesResponse = null;
        try {
            jsonStoriesResponse = Unirest.post("http://52.59.252.52:9000/search/all")
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(params.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        Gson gson = MyGson.create();

        StoriesResponse allStoriesResponse = gson.fromJson(jsonStoriesResponse.getBody().toString(), StoriesResponse.class);

        //Set the topStory attribute
        request.setAttribute("topStory", allStoriesResponse.getResult().get(3));

        //Set the popularStories
        request.setAttribute("popularStories", allStoriesResponse);

        //Set recommendedStories
        request.setAttribute(("recommendedStories"), recommendedStoriesResponse);

        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);

    }

}
