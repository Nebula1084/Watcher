package em.watcher.conroller;

import em.watcher.user.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public String login(@ModelAttribute User user, Model model) {
        System.out.println("account::"+user.getAccount());
        System.out.println("password::"+user.getPassword());
        return "Success";
    }
}
