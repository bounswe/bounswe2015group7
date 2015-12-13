package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.helpers.BaseStoryResponse;
import com.sculture.helpers.Comment;
import com.sculture.helpers.CommentListResponse;
import com.sculture.helpers.FullStoryResponse;
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
@WebServlet(name = "searchall")
public class GetStory extends HttpServlet {

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
        FullStoryResponse story = new FullStoryResponse();
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
//            if(isAllDigit){
                jsonObject.put("id", story_id);
  //          }
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/story/get")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            Object object = jsonResponse.getBody().getObject();
            Gson gson = new Gson();
//            ((JSONObject)object).remove("tags");
            story = gson.fromJson(object.toString(), FullStoryResponse.class);
        }
        // send req to comment/list
        jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("page", "1");
            jsonObject.put("size", "10");
            jsonObject.put("story_id", story_id);

            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/comment/list")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
        }
        ArrayList<Comment> comments = new ArrayList<Comment>();
        CommentListResponse commentListResponse = new CommentListResponse();
        if (jsonResponse != null) {
            Object object = jsonResponse.getBody().getObject();
            Gson gson = new Gson();
            commentListResponse = gson.fromJson(object.toString(), CommentListResponse.class);
//            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
//                Object object = jsonResponse.getBody().getArray().get(i);
//                Gson gson = new Gson();
//                comment = gson.fromJson(object.toString(), Comment.class);
//                comments.add(comment);
//            }
        }
        request.setAttribute("story", story);
        request.setAttribute("comments", commentListResponse.getResult());
        System.out.println("DSFGKJAHFIAYDHGFIUASDYGBF: "+story.getId());
        request.getRequestDispatcher("/view_story.jsp").forward(request, response);
    }

}

