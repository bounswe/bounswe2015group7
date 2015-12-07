package sculture.models.requests;

/**
 * Created by bulent on 27.11.2015.
 */
public class CommentNewRequestBody {
    private long storyId;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getStoryId() {
        return storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }
}
