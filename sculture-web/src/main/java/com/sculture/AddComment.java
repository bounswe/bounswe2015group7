package com.sculture;

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */

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

@WebServlet(name = "addcomment")
public class AddComment extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("isLoggedIn", false);
        request.setAttribute("username", "");
        if (request.getSession().getAttribute("username") != null) {
            request.setAttribute("username", request.getSession().getAttribute("username"));
            request.setAttribute("isLoggedIn", true);
        }
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("storyId", request.getParameter("story_id"));
            jsonObject.put("content", request.getParameter("form-commentbody"));
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.COMMENT_NEW)
                    .header("Content-Type", "application/json")
                    .header("access-token", request.getSession().getAttribute("access_token").toString())
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        String url = "/sculture/get/story/" + request.getParameter("story_id");
        response.sendRedirect(url);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
