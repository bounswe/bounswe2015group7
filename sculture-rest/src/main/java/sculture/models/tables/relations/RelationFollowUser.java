package sculture.models.tables.relations;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RELATION_FOLLOW_USER")
public class RelationFollowUser {

    private long FOLLOWER_USER_ID;

    private long FOLLOWED_USER_ID;

    public long getFOLLOWED_USER_ID() {
        return FOLLOWED_USER_ID;
    }

    public void setFOLLOWED_USER_ID(long FOLLOWED_USER_ID) {
        this.FOLLOWED_USER_ID = FOLLOWED_USER_ID;
    }

    public long getFOLLOWER_USER_ID() {
        return FOLLOWER_USER_ID;
    }

    public void setFOLLOWER_USER_ID(long FOLLOWER_USER_ID) {
        this.FOLLOWER_USER_ID = FOLLOWER_USER_ID;
    }
}
