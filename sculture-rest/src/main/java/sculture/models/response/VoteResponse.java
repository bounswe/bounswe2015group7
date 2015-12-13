package sculture.models.response;

import sculture.models.tables.Story;

public class VoteResponse {
    private long positive_vote;
    private long negative_vote;
    private int vote;

    public VoteResponse(int vote, Story story) {
        this.vote = vote;
        this.positive_vote = story.getPositive_vote();
        this.negative_vote = story.getNegative_vote();
    }

    public long getPositive_vote() {
        return positive_vote;
    }

    public long getNegative_vote() {
        return negative_vote;
    }

    public int getVote() {
        return vote;
    }
}
