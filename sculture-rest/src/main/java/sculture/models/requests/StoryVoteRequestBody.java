package sculture.models.requests;

public class StoryVoteRequestBody {

    private long story_id;

    private int vote;

    public long getStory_id() {
        return this.story_id;
    }

    public void setStory_id(long storyId) {
        this.story_id = storyId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
