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


@WebServlet(name = "searchall")
public class SearchAll extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("errormsg", "Something went wrong getting all the stories, please try again.");
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

        //Get all stories using /search/all

        JSONObject params = new JSONObject();
        params.put("page", 1);
        params.put("size", 10);

        HttpResponse<JsonNode> jsonStoriesResponse = null;
        try {
            jsonStoriesResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.SEARCH_ALL)
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(params.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (jsonStoriesResponse != null && !jsonStoriesResponse.getBody().getObject().has("exception")) {
            Gson gson = MyGson.create();
            StoriesResponse storiesResponse = gson.fromJson(jsonStoriesResponse.getBody().toString(), StoriesResponse.class);
            request.setAttribute("results", storiesResponse);
            request.getRequestDispatcher("/search_result.jsp").forward(request, response);
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
            request.setAttribute("errormsg", "Something went wrong getting all the stories, please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

}
