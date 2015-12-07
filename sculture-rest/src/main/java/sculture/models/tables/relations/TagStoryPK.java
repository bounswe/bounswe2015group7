package sculture.models.tables.relations;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Serializable class for making TagStory table with composite key
 */
public class TagStoryPK implements Serializable {

    private long story_id;

    private String tag_title;

    public TagStoryPK() {

    }

    public TagStoryPK(long story_id, String tag_title) {
        this.story_id = story_id;
        this.tag_title = tag_title;
    }


}
