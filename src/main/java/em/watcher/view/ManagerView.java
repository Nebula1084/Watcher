package em.watcher.view;

import em.watcher.Sessionable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ManagerView implements Sessionable {
    public static final String errorPage = "error.html";
    public static final String indexPage = "index.html";
    public static final String loginPage = "login.html";
    public static final String registerPage = "register.html";
    public static final String profilePage = "profile.html";

    @RequestMapping(value = {"/error"}, method = {RequestMethod.GET})
    public String error() {
        return errorPage;
    }
}
