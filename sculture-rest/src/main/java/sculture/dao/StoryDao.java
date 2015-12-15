package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.exceptions.InvalidReportException;
import sculture.models.tables.Story;
import sculture.models.tables.relations.ReportStory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;


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

    public void edit(Story story) {
        entityManager.merge(story);
        return;
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
    public List<Story> getAll(int page, int size) {
        Query query = entityManager.createQuery(
                "from Story");
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
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

    public List<Story> getAllPaged(int page, int size) {
        Query query = entityManager.createQuery(
                "from Story");
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;

} // class StoryDao