package sculture.models.tables.relations;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TAG_STORY")
@IdClass(TagStoryPK.class)
public class TagStory {

    @Id
    @NotNull
    private long story_id;

    @Id
    @NotNull
    private String tag_title;

    public long getStory_id() {
        return story_id;
    }

    public void setStory_id(long story_id) {
        this.story_id = story_id;
    }

    public String getTag_title() {
        return tag_title;
    }

    public void setTag_title(String tag_title) {
        this.tag_title = tag_title;
    }


    public TagStory() {
    }


}
