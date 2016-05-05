package em.watcher.view;

import em.watcher.Sessionable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class IndexView implements Sessionable {

    @RequestMapping(value = {ManagerView.indexPage}, method = {RequestMethod.GET})
    public String indexPage(@ModelAttribute(Sessionable.User) em.watcher.user.User user, Model model) {

        return ManagerView.indexPage;
    }

}
