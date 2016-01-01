package sculture.models.requests;

/**
 * Model of request body of /comment/edit controller.
 */
public class CommentEditRequestBody {
    /**
     * New content of the comment
     */
    private String content;

    /**
     * The ID of the comment which will be edited
     */
    private long comment_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }
}
