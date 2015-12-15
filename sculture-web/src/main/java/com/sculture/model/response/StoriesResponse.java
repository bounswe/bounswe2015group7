package com.sculture.model.response;

import java.util.List;

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */
public class StoriesResponse extends StoryResponse {
    private List<StoryResponse> result;

    public StoriesResponse() {
    }

    public List<StoryResponse> getResult() {
        return result;
    }

    public void setResult(List<StoryResponse> result) {
        this.result = result;
    }




}
