package com.sculture.helpers;

import java.util.List;

public class BaseStoryResponse {
    private class User {
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
    private String creation_date;
    private String update_date;
    private User last_editor = new User();
    private User owner = new User();
    private List<String> tags;
    private long positive_vote;
    private long negative_vote;
    private long report_count;


    public BaseStoryResponse() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public User getLast_editor() {
        return last_editor;
    }

    public User getOwner() {
        return owner;
    }

    public List<String> getTags() {
        return tags;
    }

    public long getPositive_vote() {
        return positive_vote;
    }

    public long getNegative_vote() {
        return negative_vote;
    }

    public long getReport_count() {
        return report_count;
    }
}
