package sculture.models.response;

public class SuccessResponse  {
    int vote_count;

    public SuccessResponse(int voteNumber) {
        vote_count=voteNumber;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}