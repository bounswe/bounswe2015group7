package sculture.models.response;


import sculture.dao.TagStoryDao;
import sculture.dao.UserDao;
import sculture.models.tables.Story;

public class FullStoryResponse extends BaseStoryResponse {
    private String content;

    public FullStoryResponse(Story story, TagStoryDao tagStoryDao, UserDao userDao) {
        super(story, tagStoryDao, userDao);
        this.content = story.getContent();
    }

    public String getContent() {
        return content;
    }
}
