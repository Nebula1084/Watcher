package em.watcher.view;

import em.watcher.Sessionable;
import em.watcher.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileView implements Sessionable {

    @RequestMapping(value = {ManagerView.profilePage}, method = {RequestMethod.GET})
    public String indexPage(@ModelAttribute(Sessionable.User) User user) {
        return ManagerView.profilePage;
    }
}
