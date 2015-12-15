package com.sculture.model.response;

import java.util.List;


public class CommentListResponse {
    private List<CommentResponse> result;

    public List<CommentResponse> getResult() {
        return result;
    }

    public void setResult(List<CommentResponse> result) {
        this.result = result;
    }
}
