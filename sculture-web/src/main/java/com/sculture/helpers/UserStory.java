package com.sculture.helpers;

import java.util.List;

/**
 * Created by Atakan Arıkan on 14.12.2015.
 */
public class UserStory {
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
    private String id;
    private String title;
    private long creation_date;
    private long update_date;
    private List<String> tags;
    private String positive_vote;
    private String negative_vote;
    private String report_count;

    @Override
    public String toString() {
        return "UserStory{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", creation_date=" + creation_date +
                ", update_date=" + update_date +
                ", tags=" + tags +
                ", positive_vote='" + positive_vote + '\'' +
                ", negative_vote='" + negative_vote + '\'' +
                ", report_count='" + report_count + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public long getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(long update_date) {
        this.update_date = update_date;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPositive_vote() {
        return positive_vote;
    }

    public void setPositive_vote(String positive_vote) {
        this.positive_vote = positive_vote;
    }

    public String getNegative_vote() {
        return negative_vote;
    }

    public void setNegative_vote(String negative_vote) {
        this.negative_vote = negative_vote;
    }

    public String getReport_count() {
        return report_count;
    }

    public void setReport_count(String report_count) {
        this.report_count = report_count;
    }
}
