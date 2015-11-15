package sculture.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sculture.dao.UserDao;
import sculture.models.Story;
import sculture.models.User;

@RestController
public class SCultureRest {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/user/new")
    public User greeting(@RequestParam("username") String name,
                         @RequestParam("password") String password,
                         @RequestParam(value = "email", required = false) String mail) {
        User user = new User();
        user.setName(name);
        user.setUsername(name);
        user.setPassword_hash(password);
        user.setEmail(mail);
        userDao.create(user);
        return user;
    }


    // TODO
    @RequestMapping("/story/get")
    public Story storyGet(@RequestParam("id") String id) {
        return new Story(id);
    }


}
