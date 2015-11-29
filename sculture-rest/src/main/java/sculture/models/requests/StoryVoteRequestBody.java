package sculture.models.requests;

import javax.validation.constraints.NotNull;

/**
 * Created by gmzrmks on 29.11.2015.
 */
public class StoryVoteRequestBody {

    private long story_id;

    /*if it is an up vote = 1, if it is a down vote = 0.*/
    private boolean up_or_down_vote;

    public long getStory_id(){ return this.story_id; }

    public void setStory_id(long storyId) {this.story_id = storyId; }

    public boolean isUp_or_down_vote(){ return this.up_or_down_vote;}

    public void setUp_or_down_vote(boolean up_or_down_vote){this.up_or_down_vote=up_or_down_vote;}
}
