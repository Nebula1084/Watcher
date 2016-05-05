package em.watcher.view;

import em.watcher.ManageStatus;
import em.watcher.Sessionable;
import em.watcher.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginView implements Sessionable {

    @RequestMapping(value = {ManagerView.loginPage}, method = {RequestMethod.GET})
    public String page(@ModelAttribute(Sessionable.Status) ManageStatus status, Model model) {
        model.addAttribute("user", new User());
        return ManagerView.loginPage;
    }

    @RequestMapping(value = {"/"}, method = {RequestMethod.GET})
    public String page(Model model) {
        model.addAttribute("user", new User());
        return ManagerView.loginPage;
    }
}
