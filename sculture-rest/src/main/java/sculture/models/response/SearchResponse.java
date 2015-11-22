package sculture.models.response;

import java.util.List;

public class SearchResponse {
    private List<BaseStoryResponse> result;

    public List<BaseStoryResponse> getResult() {
        return result;
    }

    public void setResult(List<BaseStoryResponse> result) {
        this.result = result;
    }
}
