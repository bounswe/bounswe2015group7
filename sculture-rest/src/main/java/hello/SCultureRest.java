package hello;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SCultureRest {

    @RequestMapping("/user/{username}")
    public User greeting(@PathVariable( "username" )  String name) {
       return new User(name);
    }
}
