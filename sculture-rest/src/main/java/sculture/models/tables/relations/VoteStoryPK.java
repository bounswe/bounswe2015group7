package sculture.models.tables.relations;

/**
 * Created by Fatih on 29-Nov-15.
 */

import java.io.Serializable;

/**
 * Serializable class for making TagStory table with composite key
 */
public class VoteStoryPK implements Serializable {

    private long user_id;

    private long story_id;

    public VoteStoryPK() {

    }

    public VoteStoryPK(long story_id, long user_id) {
        this.story_id = story_id;
        this.user_id = user_id;
    }


}