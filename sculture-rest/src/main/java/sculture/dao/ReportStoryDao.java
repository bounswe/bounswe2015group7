package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.Story;
import sculture.models.tables.User;
import sculture.models.tables.relations.ReportStory;
import sculture.models.tables.relations.VoteStory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ReportStoryDao {
    public void reportStory(User user, Story story) {
        if (get(user.getUser_id(), story.getStory_id()) == null) {
            ReportStory reportStory = new ReportStory();
            reportStory.setReporting_user_id(user.getUser_id());
            reportStory.setReported_story_id(story.getStory_id());
            entityManager.merge(reportStory);
            story.setReport_count(story.getReport_count() + 1);
            entityManager.merge(story);
        }
    }

    public ReportStory get(long user_id, long story_id) {
        Query query = entityManager.createQuery("from ReportStory where user_id = :user_id AND story_id = :story_id");
        query.setParameter("user_id", user_id);
        query.setParameter("story_id", story_id);

        ReportStory reportStory;
        try {
            reportStory = (ReportStory) query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
        return reportStory;
    }

    public void deleteByUserId(long user_id) {
        //TODO Update counts
        entityManager.createQuery("DELETE FROM ReportStory WHERE user_id = :user_id")
                .setParameter("user_id", user_id)
                .executeUpdate();
    }

    public void deleteByStoryId(long story_id) {
        //TODO Update counts
        entityManager.createQuery("DELETE FROM ReportStory WHERE story_id = :story_id")
                .setParameter("story_id", story_id)
                .executeUpdate();
    }


    @PersistenceContext
    private EntityManager entityManager;
}
