package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.helpers.FullStoryResponse;
import com.sculture.helpers.Story;

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

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://52.28.216.93/user";
    static final String USER = "root";
    static final String PASS = "123456";
//    Connection conn = null;

//    @Override
//    public void init() throws ServletException {
//        super.init();
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }

        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FullStoryResponse story;
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }

        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.post("http://52.28.216.93:9000/search/all")
                    .header("Content-Type", "application/json")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        ArrayList<FullStoryResponse> stories = new ArrayList<FullStoryResponse>();
        if (jsonResponse != null) {
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                Object object = jsonResponse.getBody().getArray().get(i);
                Gson gson = new Gson();
                story = gson.fromJson(object.toString(), FullStoryResponse.class);
                stories.add(story);
            }
        }
        request.setAttribute("topStory", stories.get(0));
        ArrayList<FullStoryResponse> popular = new ArrayList<FullStoryResponse>();
        popular.add(stories.get(0));
        popular.add(stories.get(1));
        popular.add(stories.get(2));
        popular.add(stories.get(3));
        System.out.println("anan");
        request.setAttribute("popularStories", popular);
        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);
    }

}
