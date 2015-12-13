package com.sculture.helpers;

import java.util.List;

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */
public class FullStoryResponse extends BaseStoryResponse {
    private List<String> media;

    public FullStoryResponse() {
    }

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
                "media=" + media +
                '}';
    }

}
