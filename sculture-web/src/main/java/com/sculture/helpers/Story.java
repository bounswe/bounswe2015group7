package com.sculture.helpers;

public class Story {

    private long story_id;

    private long owner_id;

    private String create_date;

    private long last_editor_id;

    private String last_edit_date;

    private String content;

    private long positive_vote;

    private long negative_vote;

    private long report_count;

    private String title;

    private String media;

    public Story() {
    }

    public Story(long story_id) {
        this.story_id = story_id;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public long getLast_editor_id() {
        return last_editor_id;
    }

    public void setLast_editor_id(long last_editor_id) {
        this.last_editor_id = last_editor_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(String  last_edit_date) {
        this.last_edit_date = last_edit_date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Story{" +
                "story_id=" + story_id +
                ", owner_id=" + owner_id +
                ", create_date='" + create_date + '\'' +
                ", last_editor_id=" + last_editor_id +
                ", last_edit_date='" + last_edit_date + '\'' +
                ", content='" + content + '\'' +
                ", positive_vote=" + positive_vote +
                ", negative_vote=" + negative_vote +
                ", report_count=" + report_count +
                ", title='" + title + '\'' +
                ", media='" + media + '\'' +
                '}';
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
