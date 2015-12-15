package sculture.dao;

import org.springframework.stereotype.Repository;
import sculture.models.tables.Story;
import sculture.models.tables.relations.FollowUser;
import sculture.models.tables.relations.VoteStory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class FollowUserDao {
    public void follow_user(long follower_id, long followed_id, boolean is_follow) {
        FollowUser followUser = new FollowUser();
        followUser.setIs_follow(is_follow);
        followUser.setFollowed_id(followed_id);
        followUser.setFollower_id(follower_id);
        entityManager.merge(followUser);
    }

    public FollowUser get(long follower_id, long followed_id) {
        Query queryVote = entityManager.createQuery("from FollowUser where follower_id = :follower_id AND followed_id = :followed_id");
        queryVote.setParameter("follower_id", follower_id);
        queryVote.setParameter("followed_id", followed_id);

        FollowUser followUser;

        try {
            followUser = (FollowUser) queryVote.getSingleResult();
        } catch (NoResultException e) {
            followUser = new FollowUser();
            followUser.setFollowed_id(followed_id);
            followUser.setFollower_id(follower_id);
            followUser.setIs_follow(false);
            entityManager.merge(followUser);
        }

        return followUser;
    }

    public void deleteByUserId(long user_id) {
        //TODO Update counts
        entityManager.createQuery("DELETE FROM FOLLOW_USER WHERE follower_id = :user_id OR followed_id = :user_id")
                .setParameter("user_id", user_id)
                .executeUpdate();
    }

    @PersistenceContext
    private EntityManager entityManager;

}


