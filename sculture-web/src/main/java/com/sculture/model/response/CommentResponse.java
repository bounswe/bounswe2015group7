package com.sculture.model.response;

public class CommentResponse {
    private long comment_id;
    private long story_id;
    private long owner_id;
    private String owner_username;
    private String create_date;
    private String content;
    private String last_edit_date;

    public CommentResponse() {
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


    public String getCreate_date() {
        return create_date;
    }


    public String getContent() {
        return content;
    }


    public String getLast_edit_date() {
        return last_edit_date;
    }


    public String getOwner_username() {
        return owner_username;
    }


}
