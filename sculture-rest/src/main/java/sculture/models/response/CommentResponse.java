package sculture.models.response;

import sculture.dao.UserDao;
import sculture.models.tables.Comment;

import java.util.Date;


public class CommentResponse {
    private long comment_id;
    private long story_id;
    private long owner_id;
    private String owner_username;
    private Date create_date;
    private String content;
    private Date last_edit_date;

    public CommentResponse(Comment comment, UserDao userDao) {
        this.comment_id = comment.getComment_id();
        this.story_id = comment.getStory_id();
        this.owner_id = comment.getOwner_id();
        this.create_date = comment.getCreate_date();
        this.content = comment.getContent();
        this.last_edit_date = comment.getLast_edit_date();
        this.owner_username = userDao.getById(owner_id).getUsername();
    }

    public long getComment_id() {
        return comment_id;
    }

    public long getStory_id() {
        return story_id;
    }


    public long getOwner_id() {
        return owner_id;
    }


    public Date getCreate_date() {
        return create_date;
    }


    public String getContent() {
        return content;
    }


    public Date getLast_edit_date() {
        return last_edit_date;
    }


    public String getOwner_username() {
        return owner_username;
    }




}
