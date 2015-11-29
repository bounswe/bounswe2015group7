package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.relations.VoteStory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VoteStoryDao {

    public void vote(long user_id, long story_id, boolean vote_is_positive) {
        VoteStory voteStory = new VoteStory();
        voteStory.setStory_id(story_id);
        voteStory.setUser_id(user_id);
        voteStory.setVote_is_positive(vote_is_positive);
        entityManager.merge(voteStory);
    }

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}
