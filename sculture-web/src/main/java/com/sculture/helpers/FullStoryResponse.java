package com.sculture.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */
public class FullStoryResponse extends BaseStoryResponse {
    private String media;

    public FullStoryResponse() {
    }

    public ArrayList<String> getMedia() {
        return new ArrayList<String>(Arrays.asList(media.split(",")));
    }

    public void setMedia(String media) {
        this.media = media;
    }


    @Override
    public String toString() {
        return "FullStoryResponse{" +
                "media=" + media +
                '}';
    }

}
