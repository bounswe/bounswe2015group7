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
import java.util.ArrayList;


@WebServlet(name = "login")
public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpResponse<JsonNode> jsonResponse = null;
        try {


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", request.getParameter("form-username"));
            jsonObject.put("password", request.getParameter("form-password"));
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.59.252.52:9000/user/login")
                    .header("Content-Type", "application/json")
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null) {
            request.setAttribute("isLoggedIn", true);
            request.setAttribute("username", jsonResponse.getBody().getObject().get("username"));
            request.getSession().setAttribute("username", jsonResponse.getBody().getObject().get("username"));
            request.getSession().setAttribute("userid", jsonResponse.getBody().getObject().get("id"));
            request.getSession().setAttribute("access_token", jsonResponse.getBody().getObject().get("access_token").toString());
            System.out.println(jsonResponse.getBody().getObject().get("access_token").toString());
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
        }
        //Get popular stories using /search/all

        JSONObject params = new JSONObject();
        params.put("page", 1);
        params.put("size", 4);

        HttpResponse<JsonNode> popularStoriesResponse = null;
        try {
            popularStoriesResponse = Unirest.post("http://52.59.252.52:9000:9000/search/all")
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(params.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        Gson gson = MyGson.create();
        StoriesResponse storiesResponse = gson.fromJson(popularStoriesResponse.getBody().getObject().toString(), StoriesResponse.class);

        //Set the topStory attribute
        request.setAttribute("topStory", storiesResponse.getResult().get(3));

        //Set the popularStories
        request.setAttribute("popularStories", storiesResponse);
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

}
