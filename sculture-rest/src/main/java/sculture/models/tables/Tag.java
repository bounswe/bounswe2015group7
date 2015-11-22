package sculture.models.tables;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "TAGS")
public class Tag {
    @Id
    @Column(name = "tag_title", unique = true, nullable = false)
    private String tag_title;

    @Lob
    private String tag_description;

    @Column(name = "is_location", columnDefinition = "boolean default false")
    private boolean is_location;

    @NotNull
    private long last_editor_id;

    @NotNull
    private Date last_edit_date;


    public Date getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Date last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public String getTag_title() {
        return tag_title;
    }

    public void setTag_title(String tag_title) {
        this.tag_title = tag_title;
    }

    public String getTag_description() {
        return tag_description;
    }

    public void setTag_description(String tag_description) {
        this.tag_description = tag_description;
    }

    public boolean is_location() {
        return is_location;
    }

    public void setIs_location(boolean is_location) {
        this.is_location = is_location;
    }

    public long getLast_editor_id() {
        return last_editor_id;
    }

    public void setLast_editor_id(long last_editor_id) {
        this.last_editor_id = last_editor_id;
    }
}
