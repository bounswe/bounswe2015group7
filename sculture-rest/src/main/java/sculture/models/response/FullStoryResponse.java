package sculture.models.response;


import sculture.dao.TagStoryDao;
import sculture.dao.UserDao;
import sculture.dao.VoteStoryDao;
import sculture.models.tables.Story;
import sculture.models.tables.User;

import java.util.Arrays;
import java.util.List;

public class FullStoryResponse extends BaseStoryResponse {
    private String content;
    private List<String> media;
    private int vote;

    public FullStoryResponse(Story story, User current_user, TagStoryDao tagStoryDao, UserDao userDao, VoteStoryDao voteStoryDao) {
        super(story, tagStoryDao, userDao);
        this.content = story.getContent();
        if (story.getMedia() != null)
            this.media = Arrays.asList(story.getMedia().split("\\s*,\\s*"));

        if (current_user == null)
            vote = 0;
        else {
            vote = voteStoryDao.get(current_user.getUser_id(), story.getStory_id()).getVote();
        }
    }

    public String getContent() {
        return content;
    }

    public List<String> getMedia() {
        return media;
    }

    public int getVote() {
        return vote;
    }
}
