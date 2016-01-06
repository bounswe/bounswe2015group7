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


@WebServlet(name = "search")
public class Search extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }
        String requestURL = request.getRequestURL().toString();
        String query = requestURL.substring(requestURL.lastIndexOf('/') + 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);

        HttpResponse<JsonNode> searchJsonResponse = null;
        try {
            searchJsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.SEARCH)
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(jsonObject.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        if (searchJsonResponse != null && !searchJsonResponse.getBody().getObject().has("error")) {
            Gson gson = MyGson.create();
            StoriesResponse storiesResponse = gson.fromJson(searchJsonResponse.getBody().toString(), StoriesResponse.class);
            request.setAttribute("results", storiesResponse);
            request.getRequestDispatcher("/search_result.jsp").forward(request, response);
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
            request.setAttribute("errormsg", "Something went wrong editing your story, please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
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
            searchJsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.SEARCH)
                    .header("Content-Type", "application/json")
                    .body(new JsonNode(jsonObject.toString()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (searchJsonResponse != null && !searchJsonResponse.getBody().getObject().has("exception")) {
            Gson gson = MyGson.create();
            StoriesResponse storiesResponse = gson.fromJson(searchJsonResponse.getBody().toString(), StoriesResponse.class);
            request.setAttribute("results", storiesResponse);
            request.getRequestDispatcher("/search_result.jsp").forward(request, response);
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
            request.setAttribute("errormsg", "Something went wrong getting the results of your query, please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

}
