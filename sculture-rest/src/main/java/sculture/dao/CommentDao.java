package sculture.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sculture.models.tables.Comment;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bulent on 18.11.2015.
 */

@Repository
@Transactional
public class CommentDao {

    

    /**
     * Save the comment in the database.
     * @param comment
     */
    public void create(Comment comment) {
        entityManager.persist(comment);
        return;
    }

    /**
     * Update a comment in the database.
     * @param comment
     */
    public void update(Comment comment){
        entityManager.merge(comment);
        return;
    }

    /**
     * Retrieve all comments of a story.
     */
    public List<Comment> retrieveByStory(long story_id){
        return (List<Comment>) entityManager.createQuery(
                "from Comment where story_id = :story_id")
                .setParameter("story_id", story_id).getResultList();
    }

    /**
     * Retrieve all comments of a user.
     */
    public List<Comment> retrieveByUser(long user_id){
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



    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}
