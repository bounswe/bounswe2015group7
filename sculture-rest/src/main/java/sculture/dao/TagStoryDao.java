package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.Story;
import sculture.models.tables.relations.TagStory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bulent on 18.11.2015.
 */

@Repository
@Transactional
public class TagStoryDao {


    public void create(TagStory tagStory) {
        entityManager.persist(tagStory);
        return;
    }


    public void update(TagStory tagStory) {
        entityManager.merge(tagStory);
        return;
    }

    public void delete(TagStory tagStory) {
        if (entityManager.contains(tagStory))
            entityManager.remove(tagStory);
        else
            entityManager.remove(entityManager.merge(tagStory));
        return;
    }

    public List<TagStory> getTagStoriesByStoryId(long story_id) {
        return (List<TagStory>) entityManager.createQuery(
                "from TagStory where story_id = :story_id ")
                .setParameter("story_id", story_id).getResultList();
    }

    public List<String> getTagTitlesByStoryId(long story_id) {
        List<TagStory> tagStories = getTagStoriesByStoryId(story_id);

        List<String> tag_titles = new ArrayList<>();
        for (TagStory tagStory : tagStories)
            tag_titles.add(tagStory.getTag_title());
        return tag_titles;
    }


    public List<Long> getStoryIdsByTag(String tag_title, int page, int size) {

        Query query = entityManager.createQuery(
                "from TagStory where tag_title = :tag_title ");
        query.setParameter("tag_title", tag_title);
        query.setFirstResult((page-1) * size);
        query.setMaxResults(size);
        List <TagStory> tagStories = query.getResultList();


        List<Long> story_ids = new ArrayList<>();
        for (TagStory tagStory : tagStories)
            story_ids.add(tagStory.getStory_id());
        return story_ids;

    }




    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}
