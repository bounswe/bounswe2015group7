package sculture.models.tables.relations;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by gmzrmks on 29.11.2015.
 */
@Entity
@Table(name = "REPORT_STORY")
public class ReportStory {
    @NotNull
    private long user_id;

    @NotNull
    private long story_id;

    public long getUser_id(){return this.user_id;}
    public long getStory_id(){return this.story_id;}
    public void setReporting_user_id(long reporting_user_id){ this.user_id=reporting_user_id;}
    public void setReported_story_id(long reported_story_id){ this.story_id=reported_story_id;}
}
