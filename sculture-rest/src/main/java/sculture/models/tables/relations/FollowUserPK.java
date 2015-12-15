package sculture.models.tables.relations;

import java.io.Serializable;

public class FollowUserPK implements Serializable {

    private long follower_id;

    private long followed_id;


    public FollowUserPK() {

    }

    public FollowUserPK(long follower_id, long followed_id) {
        this.follower_id = follower_id;
        this.followed_id = followed_id;
    }


}