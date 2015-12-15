package com.sculture.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */
public class SearchResponse extends BaseStoryResponse {
    private List<BaseStoryResponse> result;

    public List<BaseStoryResponse> getResult() {
        return result;
    }

    public void setResult(List<BaseStoryResponse> result) {
        this.result = result;
    }

    public SearchResponse() {
    }


}
