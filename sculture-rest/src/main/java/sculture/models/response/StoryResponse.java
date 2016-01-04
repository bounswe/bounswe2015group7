package sculture.models.response;

import sculture.dao.TagStoryDao;
import sculture.dao.UserDao;
import sculture.dao.VoteStoryDao;
import sculture.models.tables.Story;
import sculture.models.tables.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StoryResponse {
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


    public StoryResponse(Story story, sculture.models.tables.User current_user, TagStoryDao tagStoryDao, UserDao userDao, VoteStoryDao voteStoryDao) {
        this.id = story.getStory_id();
        this.title = story.getTitle();
        this.creation_date = story.getCreate_date();
        this.update_date = story.getLast_edit_date();
        this.last_editor.id = story.getLast_editor_id();
        this.owner.id = story.getOwner_id();
        this.positive_vote = story.getPositive_vote();
        this.negative_vote = story.getNegative_vote();
        this.report_count = story.getReport_count();

        this.tags = tagStoryDao.getTagTitlesByStoryId(this.id);

        this.owner.username = userDao.getById(story.getOwner_id()).getUsername();
        this.last_editor.username = userDao.getById(story.getLast_editor_id()).getUsername();

        this.content = story.getContent();
        this.media = story.getMediaList();
        if (current_user == null)
            vote = 0;
        else {
            vote = voteStoryDao.get(current_user.getUser_id(), story.getStory_id()).getVote();
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public Date getUpdate_date() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public void setLast_editor(User last_editor) {
        this.last_editor = last_editor;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setPositive_vote(long positive_vote) {
        this.positive_vote = positive_vote;
    }

    public void setNegative_vote(long negative_vote) {
        this.negative_vote = negative_vote;
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
