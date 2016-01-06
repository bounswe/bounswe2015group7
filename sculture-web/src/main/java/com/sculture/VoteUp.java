package com.sculture;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Atakan Arıkan on 06.01.2016.
 */
@WebServlet(name = "voteup")

public class VoteUp extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURL = request.getRequestURL().toString();
        String story_id = requestURL.substring(requestURL.lastIndexOf('/') + 1);
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("story_id", story_id);
            jsonObject.put("vote", 1);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.STORY_VOTE)
                    .header("Content-Type", "application/json")
                    .header("access-token", request.getSession().getAttribute("access_token").toString())
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if(jsonResponse != null){
            System.out.println(jsonResponse.getBody().toString());
        }
        response.sendRedirect("/sculture/get/story/" + story_id);
    }
}
