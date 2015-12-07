package sculture.models.response;


import sculture.dao.TagStoryDao;
import sculture.dao.UserDao;
import sculture.models.tables.Story;

import java.util.ArrayList;
import java.util.Arrays;

public class FullStoryResponse extends BaseStoryResponse {
    private String content;
    private ArrayList<String> media;

    public FullStoryResponse(Story story, TagStoryDao tagStoryDao, UserDao userDao) {
        super(story, tagStoryDao, userDao);
        this.content = story.getContent();
        this.media = (ArrayList<String>) Arrays.asList(story.getMedia().split("\\s*,\\s*"));
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getMedia() {
        return media;
    }
}
