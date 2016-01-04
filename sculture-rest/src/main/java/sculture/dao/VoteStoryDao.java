package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.Story;
import sculture.models.tables.relations.VoteStory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class VoteStoryDao {

    public Story vote(long user_id, long story_id, int vote) {
        VoteStory voteStory = get(user_id, story_id);
        Story story = entityManager.find(Story.class, story_id);

        if (voteStory == null) {
            voteStory = new VoteStory();
            voteStory.setStory_id(story_id);
            voteStory.setUser_id(user_id);
            if (vote == 1) {
                story.setPositive_vote(story.getPositive_vote() + 1);
            } else if (vote == -1)
                story.setNegative_vote(story.getNegative_vote() + 1);
        } else {
            if (vote != voteStory.getVote()) {
                if (voteStory.getVote() == -1)
                    story.setNegative_vote(story.getNegative_vote() - 1);
                if (voteStory.getVote() == 1)
                    story.setPositive_vote(story.getPositive_vote() - 1);
                if (vote == 1)
                    story.setPositive_vote(story.getPositive_vote() + 1);
                if (vote == -1)
                    story.setNegative_vote(story.getNegative_vote() + 1);
            }
        }
        voteStory.setVote(vote);
        entityManager.merge(voteStory);
        entityManager.merge(story);
        return story;
    }

    public VoteStory get(long user_id, long story_id) {
        Query queryVote = entityManager.createQuery("from VoteStory where user_id = :user_id AND story_id = :story_id");
        queryVote.setParameter("user_id", user_id);
        queryVote.setParameter("story_id", story_id);

        VoteStory voteStory;
        try {
            voteStory = (VoteStory) queryVote.getSingleResult();
        } catch (NoResultException exception) {
            voteStory = new VoteStory();
            voteStory.setVote(0);
            voteStory.setStory_id(story_id);
            voteStory.setUser_id(user_id);
            entityManager.merge(voteStory);
        }
        return voteStory;
    }


    public void deleteByUserId(long user_id) {
        //TODO Update counts
        entityManager.createQuery("DELETE FROM VoteStory WHERE user_id = :user_id")
                .setParameter("user_id", user_id)
                .executeUpdate();
    }

    public void deleteByStoryId(long story_id) {
        entityManager.createQuery("DELETE FROM VoteStory WHERE story_id = :story_id")
                .setParameter("story_id", story_id)
                .executeUpdate();
    }

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}
