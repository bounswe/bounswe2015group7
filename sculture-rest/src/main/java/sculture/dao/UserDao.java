package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class UserDao {

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    /**
     * Save the user in the database.
     */
    public void create(User user) {
        entityManager.persist(user);
        return;
    }

    /**
     * Delete the user from the database.
     */
    public void delete(User user) {
        if (entityManager.contains(user))
            entityManager.remove(user);
        else
            entityManager.remove(entityManager.merge(user));
        return;
    }

    /**
     * Return all the users stored in the database.
     */
    @SuppressWarnings("unchecked")
    public List<User> getAll() {
        return entityManager.createQuery("from User").getResultList();
    }

    /**
     * Return the user having the passed email.
     */
    public User getByEmail(String email) {
        return (User) entityManager.createQuery(
                "from User where email = :email")
                .setParameter("email", email)
                .getSingleResult();
    }

    /**
     * Return the user having the passed id.
     */
    public User getById(long id) {
        return entityManager.find(User.class, id);
    }

    /**
     * Update the passed user in the database.
     */
    public void update(User user) {
        entityManager.merge(user);
        return;
    }

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;

} // class UserDao