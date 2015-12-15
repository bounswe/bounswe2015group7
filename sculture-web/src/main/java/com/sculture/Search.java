package com.sculture;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.helpers.BaseStoryResponse;
import com.sculture.helpers.FullStoryResponse;
import com.sculture.helpers.SearchResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


@WebServlet(name = "search")
public class Search extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get");
//        request.setAttribute("isLoggedIn", false);
//        request.setAttribute("username", "");
//        if (request.getSession().getAttribute("username") != null) {
//            request.setAttribute("username", request.getSession().getAttribute("username"));
//            request.setAttribute("isLoggedIn", true);
//        }
//        request.getRequestDispatcher("/add_story.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("selam");
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", request.getParameter("main-search"));

        HttpResponse<JsonNode> searchJsonResponse = null;
        try {
            searchJsonResponse = Unirest.post("http://52.28.216.93:9000/search")
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(jsonObject.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        SearchResponse searchResponse = new SearchResponse();
        ArrayList<BaseStoryResponse> stories = new ArrayList<BaseStoryResponse>();
        if (searchJsonResponse != null && !searchJsonResponse.getBody().isArray()) {
            JSONArray jsonStories = searchJsonResponse.getBody().getObject().getJSONArray("result");
            for(int i=0; i< jsonStories.length(); i++) {
                Gson gson = new Gson();
                BaseStoryResponse story = gson.fromJson(jsonStories.get(i).toString(), BaseStoryResponse.class);
                stories.add(story);
            }
        }
        request.setAttribute("results", stories);
        request.getRequestDispatcher("/search_result.jsp").forward(request, response);
    }

}
