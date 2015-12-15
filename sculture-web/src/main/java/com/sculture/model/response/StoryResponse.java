package com.sculture.model.response;

import java.util.Date;
import java.util.List;

public class StoryResponse {
    public class User {
        private long id;
        private String username;

        public long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
    }

    private long id;
    private String title;
    private Date creation_date;
    private Date update_date;
    private User last_editor = new User();
    private User owner = new User();
    private List<String> tags;
    private long positive_vote;
    private long negative_vote;
    private long report_count;
    private String content;
    private List<String> media;
    private int vote;

    public StoryResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public User getLast_editor() {
        return last_editor;
    }

    public void setLast_editor(User last_editor) {
        this.last_editor = last_editor;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getPositive_vote() {
        return positive_vote;
    }

    public void setPositive_vote(long positive_vote) {
        this.positive_vote = positive_vote;
    }

    public long getNegative_vote() {
        return negative_vote;
    }

    public void setNegative_vote(long negative_vote) {
        this.negative_vote = negative_vote;
    }

    public long getReport_count() {
        return report_count;
    }

    public void setReport_count(long report_count) {
        this.report_count = report_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
