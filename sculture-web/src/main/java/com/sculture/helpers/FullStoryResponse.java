package com.sculture.helpers;

import java.util.List;

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */
public class FullStoryResponse extends BaseStoryResponse {
    private String content;
    private List<String> media;

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }

    public FullStoryResponse(List<String> media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "FullStoryResponse{" +
                "content='" + content + '\'' +
                ", media=" + media +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FullStoryResponse(String content) {
        this.content = content;
    }
}
