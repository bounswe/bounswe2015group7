package sculture.models.response;

//TODO We may add total follower count in this response
public class UserFollowResponse {
    private boolean is_follow;

    public UserFollowResponse(boolean is_follow) {
        this.is_follow = is_follow;
    }

    public boolean is_follow() {
        return is_follow;
    }
}
