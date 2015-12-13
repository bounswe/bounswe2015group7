package sculture.models.requests;

/**
 * Created by gmzrmks on 29.11.2015.
 */
public class StoryReportRequestBody {

    private long user_id;

    private long story_id;


    public long getUser_id(){return this.user_id ;}

    public long getStory_id(){return this.story_id;}
}
