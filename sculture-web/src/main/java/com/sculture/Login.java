package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.helpers.Story;
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
        Story story;
        HttpResponse<JsonNode> jsonResponse = null;
        try {


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", request.getParameter("form-username"));
            jsonObject.put("password", request.getParameter("form-password"));
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post("http://52.28.216.93:9000/user/login")
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
            request.getSession().setAttribute("access_token", jsonResponse.getBody().getObject().get("access_token"));
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
        }
        jsonResponse = null;
        try {
            jsonResponse = Unirest.post("http://52.28.216.93:9000/search/all")
                    .header("Content-Type", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        ArrayList<Story> stories = new ArrayList<Story>();
        if (jsonResponse != null) {
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                Object object = jsonResponse.getBody().getArray().get(i);
                Gson gson = new Gson();
                story = gson.fromJson(object.toString(), Story.class);
                stories.add(story);
            }
        }
        request.setAttribute("topStory", stories.get(0));
        ArrayList<Story> popular = new ArrayList<Story>();
        popular.add(stories.get(0));
        popular.add(stories.get(1));
        popular.add(stories.get(2));
        popular.add(stories.get(3));
        request.setAttribute("popularStories", popular);
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

}
