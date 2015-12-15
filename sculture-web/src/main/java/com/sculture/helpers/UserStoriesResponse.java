package com.sculture.helpers;

import java.util.List;

/**
 * Created by Atakan Arıkan on 14.12.2015.
 */
public class UserStoriesResponse {
    private List<UserStory> result;

    @Override
    public String toString() {
        String content = "UserStoriesResponse{";
        for(int i = 0; i < result.size(); i++){
            content+= result.get(i).toString();
        }
        return content + "}";
    }

    public List<UserStory> getResult() {
        return result;
    }

    public void setResult(List<UserStory> result) {
        this.result = result;
    }
}
