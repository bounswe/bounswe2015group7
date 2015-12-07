package sculture.models.requests;

import javax.validation.constraints.NotNull;

/**
 * Created by gmzrmks on 29.11.2015.
 */
public class StoryVoteRequestBody {

    private long story_id;
    private boolean isPositive;
    private long user_id;

    public long getStory_id(){ return this.story_id; }

    public long getUser_id() {
        return user_id;
    }

    public void setStory_id(long storyId) {this.story_id = storyId; }

    public boolean getIsPositive(){ return this.isPositive;}

    public void setPositive(boolean isPositive){this.isPositive=isPositive;}

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
