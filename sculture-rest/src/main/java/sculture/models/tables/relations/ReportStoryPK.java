package sculture.models.tables.relations;

import java.io.Serializable;

public class ReportStoryPK implements Serializable {

    private long user_id;

    private long story_id;


    public ReportStoryPK() {

    }

    public ReportStoryPK(long user_id, long story_id) {
        this.user_id = user_id;
        this.story_id = story_id;
    }


}