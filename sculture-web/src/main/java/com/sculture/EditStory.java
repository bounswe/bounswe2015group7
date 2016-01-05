package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.model.response.StoryResponse;
import com.sculture.util.MyGson;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Atakan Arıkan on 05.01.2016.
 */
@WebServlet(name = "editstory")
public class EditStory extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURL = request.getRequestURL().toString();
        String story_id = requestURL.substring(requestURL.lastIndexOf('/') + 1);
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("story_id", story_id);
            jsonObject.put("title", request.getParameter("story-title"));
            jsonObject.put("content", request.getParameter("story-content"));
            ArrayList<String> tags = new ArrayList<String>(Arrays.asList(request.getParameter("story-tags").trim().split(" ")));
            jsonObject.put("tags", tags);
            ArrayList<String> media = new ArrayList<String>(Arrays.asList(request.getParameter("story-media").trim().split(" ")));
            jsonObject.put("media", media);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.59.252.52:9000/story/edit")
                    .header("Content-Type", "application/json")
                    .header("access-token", request.getSession().getAttribute("access_token").toString())
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/sculture/get/story/" + story_id);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StoryResponse story = new StoryResponse();
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }
        String requestURL = request.getRequestURL().toString();
        String story_id = requestURL.substring(requestURL.lastIndexOf('/') + 1);
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", story_id);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.59.252.52:9000/story/get")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            Object object = jsonResponse.getBody().getObject();
            Gson gson = new MyGson().create();
//            ((JSONObject)object).remove("tags");
            story = gson.fromJson(object.toString(), StoryResponse.class);
            request.setAttribute("story", story);
            request.getRequestDispatcher("/edit_story.jsp").forward(request, response);

        }
    }

}

