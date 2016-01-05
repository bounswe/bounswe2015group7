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
                System.out.println("2");
                if (item.isFormField()) { // regular field (text, checkbox etc.)
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    if(i == 0) { // title
                        fields[0] = fieldValue;
                        System.out.println("fields[0]: " + fields[0]);
                    }
                    else if(i == 1) { // content
                        fields[1] = fieldValue;
                        System.out.println("fields[1]: " + fields[1]);
                    }
                    else if (i == 2){ // tags
                        fields[3] = fieldValue;
                        System.out.println("fields[3]: " + fields[3]);
                    }
                    i++;
                } else { // photo
                    InputStream fileContent = item.getInputStream();
                    byte[] bytes = IOUtils.toByteArray(fileContent);
                    jsonResponse = Unirest.post(Const.REST_BASE_URL + Const.Api.IMAGE_UPLOAD)
                            .header("Content-Type", "application/json")
                            .body(bytes)
                            .asJson();
                    // ... (do your job here)
                    if(jsonResponse != null && jsonResponse.getBody().getObject().getString("id") != null){
                        fields[2] += jsonResponse.getBody().getObject().getString("id") + " ";
                        System.out.println("fields[2]: " + fields[2]);
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

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
        if(jsonResponse != null){
            System.out.println(jsonResponse.getBody().toString());
        }
        response.sendRedirect("/sculture/index");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
