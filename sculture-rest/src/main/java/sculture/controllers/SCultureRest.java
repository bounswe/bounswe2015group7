package sculture.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sculture.dao.UserDao;
import sculture.models.Story;
import sculture.models.User;

@RestController
public class SCultureRest {

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.POST, value = "/user/new")
    public User greeting(@RequestBody User e) {
//        User user = new User();
//        user.setName(name);
        System.out.println(e.getName());
        userDao.create(e);
//        user.setUsername(name);
//        user.setPassword_hash(password);
//        user.setEmail(mail);
//        userDao.create(user);
        return e;
    }


    // TODO
    @RequestMapping("/story/get")
    public Story storyGet(@RequestParam("id") long id) {

        return new Story(id);
    }


}
