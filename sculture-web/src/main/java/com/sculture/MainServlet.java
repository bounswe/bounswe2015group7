package com.sculture;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.sculture.model.response.StoriesResponse;
import com.sculture.model.response.StoryResponse;
import com.sculture.util.MyGson;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by bilal on 14/10/15.
 */
@WebServlet(name = "sculture")
public class MainServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //User login state

        boolean isLoggedIn = false;
        String accessToken = null;
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");

        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
            isLoggedIn = true;
            accessToken = request.getSession().getAttribute("access_token").toString();
        }


        StoriesResponse allStories = getStories(Const.Api.SEARCH_ALL, null);
        StoriesResponse trendingStories = getStories(Const.Api.RECOMMENDATION_TRENDING, null);
        StoriesResponse likedStories = getStories(Const.Api.RECOMMENDATION_SIMILAR_TO_LIKED, accessToken);
        StoriesResponse followedStories = getStories(Const.Api.RECOMMENDATION_FROM_FOLLOWED_USER, accessToken);


        // Merge liked and followed, removing duplicates
        HashSet<StoryResponse> recommendedSet = new HashSet<StoryResponse>();
        if(likedStories != null && likedStories.getResult() != null) recommendedSet.addAll(likedStories.getResult());
        if(followedStories != null && followedStories.getResult() != null) recommendedSet.addAll(followedStories.getResult());

        ArrayList<StoryResponse> recommendedList = new ArrayList<StoryResponse>();
        recommendedList.addAll(recommendedSet);

        StoriesResponse recommendedStories = new StoriesResponse();
        recommendedStories.setResult(recommendedList);


        request.setAttribute("allStories", allStories);
        request.setAttribute("trendingStories", trendingStories);
        request.setAttribute("recommendedStories", recommendedStories);

        request.getRequestDispatcher("/frontend_homepage.jsp").forward(request, response);

    }



    StoriesResponse getStories(String endPoint, String accessToken) {
        StoriesResponse storiesResponse = null;

        JSONObject params = new JSONObject();
        params.put("page", 1);
        params.put("size", 4);

        HttpRequestWithBody requestWithBody = Unirest.post(Const.REST_BASE_URL + endPoint)
                .header("Content-Type", "application/json");
        if(accessToken != null) {
            requestWithBody.header("access-token", accessToken);
        }

        try {
            HttpResponse<JsonNode> jsonStoriesResponse = requestWithBody.body(new JsonNode(params.toString())).asJson();
            Gson gson = MyGson.create();
            storiesResponse = gson.fromJson(jsonStoriesResponse.getBody().toString(), StoriesResponse.class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return storiesResponse;
    }

}
