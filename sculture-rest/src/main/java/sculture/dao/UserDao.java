package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.User;
import sculture.models.tables.relations.RelationFollowUser;

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
        return;
    }

    public void follow(User user, long id, boolean isFollow) {
        RelationFollowUser relationFollowUser = new RelationFollowUser();
        relationFollowUser.setFOLLOWER_USER_ID(user.getUser_id());
        relationFollowUser.setFOLLOWED_USER_ID(id);
        if (!isFollow) {
            if (entityManager.contains(relationFollowUser))
                entityManager.remove(relationFollowUser);
            else
                entityManager.remove(entityManager.merge(relationFollowUser));
        } else {
            entityManager.persist(relationFollowUser);
        }
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

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;


} // class UserDao