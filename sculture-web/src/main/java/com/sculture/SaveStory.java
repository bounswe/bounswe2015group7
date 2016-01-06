package com.sculture;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sculture.model.response.StoryResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@WebServlet(name = "savestory")
@MultipartConfig
public class SaveStory extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpResponse<JsonNode> jsonResponse = null;
        StoryResponse story = null;

        String[] fields = new String[4];
        fields[2] = "";
        try {
            JSONObject jsonObject = new JSONObject();
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            int i = 0;
            for (FileItem item : items) {
                if (item.isFormField()) { // regular field (text, checkbox etc.)
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    if(i == 0) { // title
                        fields[0] = fieldValue;
                    }
                    else if(i == 1) { // content
                        fields[1] = fieldValue;
                    }
                    else if (i == 2){ // tags
                        fields[3] = fieldValue;
                    }
                    i++;
                } else if(item.getString().length() > 0) { // photo
                    InputStream fileContent = item.getInputStream();
                    String fieldName = item.getString();
                    byte[] bytes = IOUtils.toByteArray(fileContent);
                    jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.IMAGE_UPLOAD)
                            .header("Content-Type", "application/json")
                            .body(bytes)
                            .asJson();
                    if(jsonResponse != null && !jsonResponse.getBody().getObject().has("exception") && jsonResponse.getBody().getObject().getString("id") != null){
                        fields[2] += jsonResponse.getBody().getObject().getString("id") + " ";
                    } else {
                        request.setAttribute("isLoggedIn", false);
                        request.setAttribute("username", "");
                        request.setAttribute("errormsg", "Something went wrong while adding your story, please try again.");
                        request.getRequestDispatcher("/error.jsp").forward(request, response);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        String redirectUrl = "/sculture/get/story/";
        try {
            JSONObject jsonObject = new JSONObject();
            ArrayList<String> tags = new ArrayList<String>(Arrays.asList(fields[3].trim().split(" ")));
            ArrayList<String> media = new ArrayList<String>(Arrays.asList(fields[2].trim().split(" ")));

            jsonObject.put("title", fields[0]);
            jsonObject.put("content", fields[1]);
            jsonObject.put("media", media);
            jsonObject.put("tags", tags);
            JsonNode jsonNode = new JsonNode(jsonObject.toString());
            jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.STORY_CREATE)
                    .header("Content-Type", "application/json")
                    .header("access-token", request.getSession().getAttribute("access_token").toString())
                    .body(jsonNode)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        if (jsonResponse != null && !jsonResponse.getBody().getObject().has("exception")) {
            redirectUrl+=jsonResponse.getBody().getObject().get("id");
        } else {
            request.setAttribute("isLoggedIn", false);
            request.setAttribute("username", "");
            request.setAttribute("errormsg", "Something went wrong editing your story, please try again.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
        response.sendRedirect(redirectUrl);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
