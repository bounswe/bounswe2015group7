package sculture.models.response;

import sculture.models.tables.Tag;

import java.util.Date;

public class TagResponse {

    private String tag_title;
    private String description;
    private boolean is_location;
    private long last_editor_id;
    private String last_editor_name;
    private Date last_edit_date;

    public TagResponse(Tag tag,String last_editor_name) {
        this.last_editor_name = last_editor_name;
        this.tag_title = tag.getTag_title();
        this.description = tag.getTag_description();
        this.is_location = tag.is_location();
        this.last_editor_id = tag.getLast_editor_id();
        this.last_edit_date = tag.getLast_edit_date();
    }

    public String getLast_editor_name() {
        return last_editor_name;
    }

    public void setLast_editor_name(String last_editor_name) {
        this.last_editor_name = last_editor_name;
    }

    public boolean is_location() {
        return is_location;
    }

    public boolean getIs_location() {
        return is_location;
    }

    public void setIs_location(boolean is_location) {
        this.is_location = is_location;
    }

    public String getTag_title() {
        return tag_title;
    }

    public void setTag_title(String tag_title) {
        this.tag_title = tag_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLast_editor_id() {
        return last_editor_id;
    }

    public void setLast_editor_id(long last_editor_id) {
        this.last_editor_id = last_editor_id;
    }

    public Date getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Date last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

}
