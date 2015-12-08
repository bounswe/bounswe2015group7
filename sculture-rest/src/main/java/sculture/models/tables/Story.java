package sculture.models.tables;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name = "STORY")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long story_id;

    @NotNull
    private long owner_id;

    @NotNull
    private Date create_date;

    @NotNull
    private long last_editor_id;

    @NotNull
    private Date last_edit_date;

    @Lob//Large object
    @NotNull
    private String content;

    @Column(name = "positive_vote", nullable = false) //Default automatically 0
    private long positive_vote;

    @Column(name = "negative_vote", nullable = false) //Default automatically 0
    private long negative_vote;

    @Column(name = "report_count", nullable = false) //Default automatically 0
    private long report_count;

    @NotNull
    private String title;

    @Column(name = "media", nullable = false) //Default automatically 0
    private String media;
    
    public Story() {
    }

    public Story(long story_id) {
        this.story_id = story_id;
    }


    public long getStory_id() {
        return story_id;
    }

    public void setStory_id(long story_id) {
        this.story_id = story_id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(long owner_id) {
        this.owner_id = owner_id;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public long getLast_editor_id() {
        return last_editor_id;
    }

    public void setLast_editor_id(long last_editor_id) {
        this.last_editor_id = last_editor_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Date last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public long getPositive_vote(){return positive_vote; }

    public void setPositive_vote(long positive_vote){this.positive_vote=positive_vote; }

    public long getNegative_vote(){return negative_vote; }

    public void setNegative_vote(long negative_vote){this.negative_vote=negative_vote; }

    public long getReport_count(){return report_count; }

    public void setReport_count(long report_count){this.report_count=report_count; }

    public String getTitle(){return title; }

    public void setTitle(String title){this.title=title; }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
