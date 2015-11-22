package sculture.models.response;


import sculture.models.tables.Story;

import java.util.List;

public class FullStoryResponse extends BaseStoryResponse {
    private String content;

    public FullStoryResponse(Story story, List<String> tags, String owner_username, String editor_username) {
        super(story, tags, owner_username, editor_username);
        this.content = story.getContent();
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
