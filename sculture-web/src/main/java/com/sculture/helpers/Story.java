package com.sculture.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Story {

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

    private long story_id;
    private String title;
    private String creation_date;
    private String update_date;
    private String content;
    private User last_editor = new User();
    private User owner = new User();
    private List<String> tags;
    private long positive_vote;
    private long negative_vote;
    private long report_count;

    private String media;

    @Override
    public String toString() {
        return "Story{" +
                "story_id=" + story_id +
                ", title='" + title + '\'' +
                ", creation_date='" + creation_date + '\'' +
                ", update_date='" + update_date + '\'' +
                ", content='" + content + '\'' +
                ", last_editor=" + last_editor +
                ", owner=" + owner +
                ", tags=" + tags +
                ", positive_vote=" + positive_vote +
                ", negative_vote=" + negative_vote +
                ", report_count=" + report_count +
                ", media='" + media + '\'' +
                '}';
    }

    public ArrayList<String> getMedia() {
        return new ArrayList<String>(Arrays.asList(media.split(",")));
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public long getStory_id() {
        return story_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreate_date() {
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
