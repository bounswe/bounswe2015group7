package sculture.dao;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Repository;
import sculture.exceptions.InvalidReportException;
import sculture.models.tables.Story;
import sculture.models.tables.relations.ReportStory;
import sculture.models.tables.relations.VoteStory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;


@Repository
@Transactional
public class StoryDao {
    /**
     * Save the story in the database.
     */
    public void create(Story story) {
        entityManager.persist(story);
        return;
    }

    public void reportStory(long userId, long storyId) {
        ReportStory reportStory = new ReportStory();
        reportStory.setReporting_user_id(userId);
        reportStory.setReported_story_id(storyId);
        if (entityManager.contains(reportStory)) {
            throw new InvalidReportException();
        }
        entityManager.persist(reportStory);
        return;
    }

    public int reportCount(long story_id){
        return entityManager.createQuery(
                "from ReportStory where story_id = :story_id ")
                .setParameter("story_id", story_id).getResultList().size();
    }
    
    public Story voteStory(long storyId, boolean isPositive, long userId) {
        VoteStory relationVoteStoryUser = new VoteStory();
        relationVoteStoryUser.setStory_id(storyId);
        relationVoteStoryUser.setUser_id(userId);
        relationVoteStoryUser.setVote_is_positive(isPositive);
        Story story = getById(storyId);

        if (entityManager.contains(relationVoteStoryUser)) {
            return story;
        }else{
            entityManager.persist(relationVoteStoryUser);

            if (isPositive) {
                story.setPositive_vote(story.getPositive_vote() + 1);
            } else {
                story.setNegative_vote(story.getNegative_vote() + 1);
            }
            entityManager.merge(story);
        }
        return story;
    }

    /**
     * Delete the story from the database.
     */
    public void delete(Story story) {
        if (entityManager.contains(story))
            entityManager.remove(story);
        else
            entityManager.remove(entityManager.merge(story));
        return;
    }

    /**
     * Return all the stories stored in the database.
     */
    @SuppressWarnings("unchecked")
    public List<Story> getAll() {
        return entityManager.createQuery("from Story").getResultList();
    }

    /**
     * Return the stories of a specific owner.
     */
    @SuppressWarnings("unchecked")
    public List<Story> getByOwner(long owner_id, int page, int size) {

        Query query = entityManager.createQuery(
                "from Story where owner_id = :owner_id ");
        query.setParameter("owner_id", owner_id);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    /**
     * Return the story having the passed id.
     */
    public Story getById(long id) {
        return entityManager.find(Story.class, id);
    }

    /**
     * Update the passed story in the database.
     */
    public void update(Story story) {
        entityManager.merge(story);
        return;
    }

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;

} // class StoryDao