package sculture.models.response;

import java.util.List;

public class SearchResponse {
    private List<StoryResponse> result;

    public List<StoryResponse> getResult() {
        return result;
    }

    public void setResult(List<StoryResponse> result) {
        this.result = result;
    }
}
