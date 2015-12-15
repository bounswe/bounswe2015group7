package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CommentDao {


    /**
     * Save the comment in the database.
     *
     * @param comment
     */
    public void create(Comment comment) {
        entityManager.persist(comment);
        return;
    }

    /**
     * Update a comment in the database.
     *
     * @param comment
     */
    public void update(Comment comment) {
        entityManager.merge(comment);
        return;
    }

    /**
     * Retrieve all comments of a story.
     */
    public List<Comment> retrieveByStory(long story_id, int page, int size) {
        Query query = entityManager.createQuery(
                "from Comment where story_id = :story_id ORDER BY create_date DESC");
        query.setParameter("story_id", story_id).getResultList();
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    /**
     * Retrieve all comments of a user.
     */
    public List<Comment> retrieveByUser(long user_id) {
        return (List<Comment>) entityManager.createQuery(
                "from Comment where user_id = :user_id")
                .setParameter("user_id", user_id).getResultList();
    }

    /**
     * Retrieve the comment having the passed id.
     */
    public Comment getById(long id) {
        return entityManager.find(Comment.class, id);
    }

    /**
     * Delete the comment from the database.
     */
    public void delete(Comment comment) {
        if (entityManager.contains(comment))
            entityManager.remove(comment);
        else
            entityManager.remove(entityManager.merge(comment));
        return;
    }

    public void deleteByUserId(long owner_id) {
        entityManager.createQuery("DELETE FROM COMMENT WHERE owner_id = :owner_id")
                .setParameter("owner_id", owner_id)
                .executeUpdate();
    }

    public void deleteByStoryId(long story_id) {
        entityManager.createQuery("DELETE FROM COMMENT WHERE story_id = :story_id")
                .setParameter("story_id", story_id)
                .executeUpdate();
    }

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}
