package em.watcher.view;

import em.watcher.Sessionable;
import em.watcher.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterView implements Sessionable {
    @RequestMapping(value = {ManagerView.registerPage}, method = {RequestMethod.GET})
    public String indexPage(Model model) {
        model.addAttribute("user", new User());
        return ManagerView.registerPage;
    }
}
