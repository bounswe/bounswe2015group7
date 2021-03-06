package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class UserDao {


    /**
     * Save the user in the database.
     */
    public void create(User user) {
        entityManager.persist(user);
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
                "from User where email = :email ")
                .setParameter("email", email)
                .getSingleResult();
    }

    /**
     * Return the user having the access_token email.
     */
    public User getByAccessToken(String access_token) {
        return (User) entityManager.createQuery(
                "from User where access_token = :access_token ")
                .setParameter("access_token", access_token)
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

    public User getByUsername(String username) {
        return (User) entityManager.createQuery(
                "from User where username = :username ")
                .setParameter("username", username)
                .getSingleResult();

    }


    public void deleteByEmail(String email) {
        entityManager.createQuery("DELETE FROM User WHERE email = :email")
                .setParameter("email", email)
                .executeUpdate();
    }
    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;


} // class UserDao