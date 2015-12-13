package sculture.models.tables.relations;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by gmzrmks on 29.11.2015.
 */
@Entity
@Table(name = "REPORT_STORY")
public class ReportStory {

    @Id
    private long id;
    

    @NotNull
    private long user_id;

    @NotNull
    private long story_id;

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public void setStory_id(long story_id) {
        this.story_id = story_id;
    }
    
    public long getUser_id() {
        return this.user_id;
    }

    public long getStory_id() {
        return this.story_id;
    }

    public void setReporting_user_id(long reporting_user_id) {
        this.user_id = reporting_user_id;
    }

    public void setReported_story_id(long reported_story_id) {
        this.story_id = reported_story_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
