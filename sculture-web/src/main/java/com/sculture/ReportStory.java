package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.helpers.*;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Atakan Arıkan on 14.12.2015.
 */
@WebServlet(name = "reportstory")
public class ReportStory extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURL = request.getRequestURL().toString();
        String story_id = requestURL.substring(requestURL.lastIndexOf('/') + 1);
        String user_id = request.getSession().getAttribute("userid").toString();
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("story_id", story_id);
            jsonObject.put("user_id", user_id);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/story/report")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/get/story/" + story_id);
    }

}

