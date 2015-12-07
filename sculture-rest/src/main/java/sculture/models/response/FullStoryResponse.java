package sculture.models.response;


import sculture.dao.TagStoryDao;
import sculture.dao.UserDao;
import sculture.models.tables.Story;

import java.util.Arrays;
import java.util.List;

public class FullStoryResponse extends BaseStoryResponse {
    private String content;
    private List<String> media;

    public FullStoryResponse(Story story, TagStoryDao tagStoryDao, UserDao userDao) {
        super(story, tagStoryDao, userDao);
        this.content = story.getContent();
        this.media =  Arrays.asList(story.getMedia().split("\\s*,\\s*"));
    }

    public String getContent() {
        return content;
    }

    public List<String> getMedia() {
        return media;
    }
}
