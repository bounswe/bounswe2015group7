package sculture.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/user/{username}")
    public User greeting(@PathVariable("username") String name) {
        User user = new User();
        user.setName(name);
        user.setEmail(name);
        userDao.create(user);
        return user;
    }

    @RequestMapping("/story/get")
    public Story storyGet(@RequestParam("id") String id) {
        return new Story(id);
    }


}
