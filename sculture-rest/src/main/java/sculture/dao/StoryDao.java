package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.Story;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class StoryDao {

    /**
     * Creates a story entry on database
     *
     * @param story The story which will be created
     */
    public void create(Story story) {
        entityManager.persist(story);
    }

    /**
     * Updates a story on database
     *
     * @param story The story which will be updated
     */
    public void update(Story story) {
        entityManager.merge(story);
    }

    /**
     * Deletes a story object from database
     *
     * @param story The story object which will be deleted
     */
    public void delete(Story story) {
        if (entityManager.contains(story))
            entityManager.remove(story);
        else
            entityManager.remove(entityManager.merge(story));
    }

    /**
     * Deletes a story from database
     *
     * @param story_id ID of the story which will be deleted
     */
    public void delete(long story_id) {
        entityManager.createQuery("DELETE FROM Story WHERE story_id = :story_id")
                .setParameter("story_id", story_id)
                .executeUpdate();
    }

    /**
     * Gets all stories on the database
     *
     * @param page Page number (1-indexed)
     * @param size Size of the page
     * @return List of stories
     */
    public List<Story> getAll(int page, int size) {
        Query query = entityManager.createQuery(
                "from Story ORDER BY create_date DESC");
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    /**
     * Gets stories which owned by a user
     *
     * @param owner_id The owner ID of the story
     * @param page     Page number (1-indexed)
     * @param size     Size of the page
     * @return List of stories which owned by given user
     */
    public List<Story> getByOwner(long owner_id, int page, int size) {

        Query query = entityManager.createQuery(
                "from Story where owner_id = :owner_id ORDER BY create_date DESC");
        query.setParameter("owner_id", owner_id);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    /**
     * Gets a story from database by id
     *
     * @param id ID of the story
     * @return Story
     */
    public Story getById(long id) {
        return entityManager.find(Story.class, id);
    }

    /**
     * A list of recent stories from followed users
     *
     * @param current_user_id The current user who follow others
     * @param page            Page number (1-indexed)
     * @param size            Size of the page
     * @return List of stories
     */
    public List<Story> storiesFromFollowedUsers(long current_user_id, int page, int size) {
        Query query = entityManager.createQuery(
                "FROM Story WHERE EXISTS (SELECT '*' FROM FollowUser WHERE followed_id = owner_id AND follower_id = :current_user_id AND is_follow = true) ORDER BY create_date DESC");
        query.setParameter("current_user_id", current_user_id);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    /**
     * Gets a list of trending stories of the system
     * It finds the trending stories by this formula
     * (p-1) / (t+2)^1.5 where p is like count of the story
     * and the t is age of story in hours
     * (source: https://moz.com/blog/reddit-stumbleupon-delicious-and-hacker-news-algorithms-exposed)
     * <p>
     * P.S. This function uses native SQL query instead of HQL(Hibernate Query Language)
     * If the column names or table names of Story or VoteStory entities are changed
     * update this query.
     *
     * @param page Page number (1 indexed)
     * @param size Size number
     * @return List of stories
     */
    public List<Story> getTrendingStories(int page, int size) {
        Query q = entityManager.createNativeQuery("SELECT s.* FROM STORY s LEFT JOIN (" +
                "    SELECT vs.story_id, COUNT(*) AS total  " +
                "    FROM VOTE_STORY vs " +
                "    WHERE vs.vote = 1 " +
                "    GROUP BY vs.story_id ) " +
                "  AS jj " +
                "  on jj.story_id = s.story_id " +
                "  ORDER BY (total-1 )/POW(create_date/3600000 + 2, 1.5)  DESC, create_date DESC", Story.class);

        q.setFirstResult((page - 1) * size);
        q.setMaxResults(size);

        return q.getResultList();
    }

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}