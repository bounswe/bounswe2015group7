package com.sculture.controller;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.helpers.BaseStoryResponse;
import com.sculture.helpers.Story;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


@WebServlet(name = "savestory")
public class SaveStory extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpResponse<JsonNode> jsonResponse = null;
        BaseStoryResponse story = null;

        ArrayList<String> tags = new ArrayList<String>(Arrays.asList(request.getParameter("story-tags").split(" ")));


        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", request.getParameter("story-title"));
            jsonObject.put("content", request.getParameter("story-content"));
            jsonObject.put("tags", tags);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/story/create")
                    .header("Content-Type", "application/json")
                    .header("access-token", request.getSession().getAttribute("access_token").toString())
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/index");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
