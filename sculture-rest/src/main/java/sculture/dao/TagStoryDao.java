package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.relations.TagStory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TagStoryDao {

    /**
     * Creates TagStory object
     *
     * @param tagStory TagStory object which will be created
     */
    public void create(TagStory tagStory) {
        entityManager.persist(tagStory);
        return;
    }

    /**
     * Updates TagStory object
     *
     * @param tagStory TagStory object which will be updated
     */
    public void update(TagStory tagStory) {
        entityManager.merge(tagStory);
        return;
    }

    /**
     * Deletes TagStory object
     *
     * @param tagStory TagStory object which will be deleted
     */
    public void delete(TagStory tagStory) {
        if (entityManager.contains(tagStory))
            entityManager.remove(tagStory);
        else
            entityManager.remove(entityManager.merge(tagStory));
        return;
    }

    /**
     * Gets TagStory objects of a story
     *
     * @param story_id story id
     * @return TagStory objects
     */
    public List<TagStory> getTagStoriesByStoryId(long story_id) {
        return (List<TagStory>) entityManager.createQuery(
                "from TagStory where story_id = :story_id ")
                .setParameter("story_id", story_id).getResultList();
    }

    /**
     * Gets tags of a story
     *
     * @param story_id Story id
     * @return Tags of story
     */
    public List<String> getTagTitlesByStoryId(long story_id) {
        List<TagStory> tagStories = getTagStoriesByStoryId(story_id);

        List<String> tag_titles = new ArrayList<>();
        for (TagStory tagStory : tagStories)
            tag_titles.add(tagStory.getTag_title());
        return tag_titles;
    }

    /**
     * Gets a distinct list of tags of stories which are liked by given user
     *
     * @param current_user_id The user id of the user
     * @return List of tags
     */
    public List<String> getTagsLikedStories(long current_user_id) {
        Query query = entityManager.createQuery(
                "SELECT DISTINCT ts.tag_title from TagStory ts WHERE EXISTS (SELECT '*' FROM VoteStory vs WHERE vs.story_id = ts.story_id AND vs.vote = 1 AND vs.user_id = :current_user_id)");
        query.setParameter("current_user_id", current_user_id);
        return query.getResultList();
    }

    /**
     * Deletes tag-story relation using story id
     *
     * @param story_id ID of the story
     */
    public void deleteByStoryId(long story_id) {
        entityManager.createQuery("DELETE FROM TagStory WHERE story_id = :story_id")
                .setParameter("story_id", story_id)
                .executeUpdate();
    }

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}
