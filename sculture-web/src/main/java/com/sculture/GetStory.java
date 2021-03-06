package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.model.Comment;
import com.sculture.model.response.CommentListResponse;
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

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */
@WebServlet(name = "getstory")
public class GetStory extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.STORY_GET)
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
        }
        // send req to comment/list
        jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("page", "1");
            jsonObject.put("size", "10");
            jsonObject.put("story_id", story_id);

            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.COMMENT_LIST)
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
        }
        ArrayList<Comment> comments = new ArrayList<Comment>();
        CommentListResponse commentListResponse = new CommentListResponse();
        if (jsonResponse != null && !jsonResponse.getBody().getObject().has("exception")) {
            Object object = jsonResponse.getBody().getObject();
            Gson gson = MyGson.create();
            commentListResponse = gson.fromJson(object.toString(), CommentListResponse.class);
        } else {
            request.setAttribute("errormsg", "Something went wrong getting this story, please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
        request.setAttribute("story", story);
        request.setAttribute("comments", commentListResponse.getResult());
        request.getRequestDispatcher("/view_story.jsp").forward(request, response);
    }

}

