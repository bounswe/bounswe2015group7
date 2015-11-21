package sculture.controllers;

import com.mysql.jdbc.Clob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sculture.Utils;
import sculture.dao.CommentDao;
import sculture.dao.StoryDao;
import sculture.dao.UserDao;
import sculture.exceptions.WrongPasswordException;
import sculture.models.Story;
import sculture.models.User;

import java.util.Date;

@RestController
public class SCultureRest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private CommentDao commentDao;

    @RequestMapping(method = RequestMethod.POST, value = "/user/register")
    public User user_register(@RequestParam("email") String email,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password,
                              @RequestParam(value = "fullname", required = false) String fullname,
                              @RequestParam(value = "facebook_id", required = false) String facebook_id,
                              @RequestParam(value = "facebook_token", required = false) String facebook_token) {
        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword_hash(Utils.password_hash(password));
        if (fullname != null) u.setFullname(fullname);
        if (facebook_id != null) u.setFacebook_id(facebook_id);
        if (facebook_token != null) u.setFacebook_token(facebook_token);
        u.setAccess_token(Utils.access_token_generate());

        userDao.create(u);
        return u;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/login")
    public User user_login(@RequestParam("email") String email,
                           @RequestParam("password") String password) {
        User u = userDao.getByEmail(email);
        if (u.getPassword_hash().equals(Utils.password_hash(password)))
            return u;
        else
            throw new WrongPasswordException();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/story/create")
    public Story story_create(@RequestParam("owner_id") long owner,
                              @RequestParam("content") Clob content) {
        Story s = new Story();
        s.setContent(content);
        s.setOwner_id(owner);
        s.setLast_editor_id(owner);
        s.setCreate_date(new Date());
        s.setLast_edit_date(new Date());
        s.setNegative_vote(0);
        s.setPositive_vote(0);
        s.setReport_count(0);
        storyDao.create(s);
        return s;
    }

    // TODO
    @RequestMapping("/story/get")
    public Story storyGet(@RequestParam("id") long id) {
        return storyDao.getById(id);
    }


}
