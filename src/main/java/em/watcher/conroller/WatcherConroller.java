package em.watcher.conroller;

import em.watcher.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WatcherConroller {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String indexPage(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

}
