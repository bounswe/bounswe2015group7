package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.model.response.StoryResponse;
import com.sculture.model.response.StoriesResponse;
import com.sculture.util.MyGson;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "search")
public class Search extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            searchJsonResponse = Unirest.post("http://52.59.252.52:9000:9000/search")
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(jsonObject.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        Gson gson = MyGson.create();
        StoriesResponse storiesResponse = gson.fromJson(searchJsonResponse.getBody().toString(), StoriesResponse.class);

        request.setAttribute("results", storiesResponse);
        request.getRequestDispatcher("/search_result.jsp").forward(request, response);
    }

}
