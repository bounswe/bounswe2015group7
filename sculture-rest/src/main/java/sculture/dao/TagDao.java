package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.Tag;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bulent on 18.11.2015.
 */

@Repository
@Transactional
public class TagDao {


    /**
     * Save the comment in the database.
     * @param tag
     */
    public void create(Tag tag) {
        entityManager.persist(tag);
        return;
    }

    /**
     * Update a comment in the database.
     * @param tag
     */
    public void update(Tag tag){
        entityManager.merge(tag);
        return;
    }



    /**
     * Retrieve the comment having the passed id.
     */
    public Tag getByTitle(String tag_title) {
        return entityManager.find(Tag.class, tag_title);
    }

    /**
     * Delete the comment from the database.
     */
    public void delete(Tag tag) {
        if (entityManager.contains(tag))
            entityManager.remove(tag);
        else
            entityManager.remove(entityManager.merge(tag));
        return;
    }

    public void deleteByUserId(long last_editor_id) {
        entityManager.createQuery("DELETE FROM Tag WHERE last_editor_id = :last_editor_id")
                .setParameter("last_editor_id", last_editor_id)
                .executeUpdate();
    }

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;
}
