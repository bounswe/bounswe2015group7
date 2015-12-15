package sculture.models.tables.relations;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FOLLOW_USER")
@IdClass(FollowUserPK.class)
public class FollowUser {

    @Id
    @NotNull
    private long follower_id;

    @Id
    @NotNull
    private long followed_id;

    @Column(name = "is_follow", nullable = false, columnDefinition = "boolean default false")
    private boolean is_follow;

    public FollowUser() {
    }

    public long getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(long follower_id) {
        this.follower_id = follower_id;
    }

    public long getFollowed_id() {
        return followed_id;
    }

    public void setFollowed_id(long followed_id) {
        this.followed_id = followed_id;
    }

    public boolean is_follow() {
        return is_follow;
    }

    public void setIs_follow(boolean is_follow) {
        this.is_follow = is_follow;
    }
}
