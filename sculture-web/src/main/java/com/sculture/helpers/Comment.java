package com.sculture.helpers;
import java.util.Date;

/**
 * Created by Atakan Arıkan on 13.12.2015.
 */



public class Comment implements Comparable<Comment>{


    private long comment_id;

    private long story_id;

    private long owner_id;

    private Date create_date;

    private String content;

    private Date last_edit_date;

    public Comment() {
    }

    public Comment(long comment_id) {
        this.comment_id = comment_id;
    }


    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public long getStory_id() {
        return story_id;
    }

    public void setStory_id(long story_id) {
        this.story_id = story_id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(long owner_id) {
        this.owner_id = owner_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Date last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id=" + comment_id +
                ", story_id=" + story_id +
                ", owner_id=" + owner_id +
                ", create_date=" + create_date +
                ", content='" + content + '\'' +
                ", last_edit_date=" + last_edit_date +
                '}';
    }

    @Override
    public int compareTo(Comment o) {
        return getCreate_date().compareTo(o.getCreate_date());
    }
}
