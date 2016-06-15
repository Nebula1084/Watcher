package em.watcher.conroller;

import em.watcher.Sessionable;
import em.watcher.WatcherStatus;
import em.watcher.device.DeviceService;
import em.watcher.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WatcherView implements Sessionable {
    public static final String errorPage = "error.html";
    public static final String indexPage = "index.html";
    public static final String loginPage = "login.html";
    public static final String registerPage = "register.html";
    public static final String profilePage = "profile.html";
    public static final String controlRgstPage = "register_control.html";
    public static final String reportRgstPage = "register_report.html";
    public static final String deviceRgstPage = "register_device.html";
    public static final String helpPage = "help.html";
    public static final String devicePage = "device.html";
    public static final String activePage = "active.html";

    @Autowired
    DeviceService deviceService;

    @RequestMapping(value = {"/error"}, method = {RequestMethod.GET})
    public String error() {
        return errorPage;
    }

    @RequestMapping(value = "/" + WatcherView.loginPage)
    public String page(@ModelAttribute(Sessionable.Status) WatcherStatus status, Model model) {
        model.addAttribute("user", new User());
        return WatcherView.loginPage;
    }

    @RequestMapping(value = "/")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return WatcherView.loginPage;
    }

    @RequestMapping(value = "/" + WatcherView.registerPage)
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return WatcherView.registerPage;
    }

    @RequestMapping(value = "/" + WatcherView.profilePage)
    public String profielPage(@ModelAttribute(Sessionable.User) User user) {
        return WatcherView.profilePage;
    }

    @RequestMapping(value = "/" + WatcherView.indexPage)
    public String indexPage(@ModelAttribute(Sessionable.User) User user, Model model) {
        return WatcherView.indexPage;
    }

    @RequestMapping(value = "/" + WatcherView.controlRgstPage)
    public String controlRgstPage(@ModelAttribute(Sessionable.User) User user, Model model) {
        return WatcherView.controlRgstPage;
    }

    @RequestMapping(value = "/" + WatcherView.reportRgstPage)
    public String reportRgstPage(@ModelAttribute(Sessionable.User) User user, Model model) {
        return WatcherView.reportRgstPage;
    }

    @RequestMapping(value = "/" + WatcherView.deviceRgstPage)
    public String deviceRgstPage(@ModelAttribute(Sessionable.User) User user, Model model) {
        return WatcherView.deviceRgstPage;
    }

    @RequestMapping(value = "/" + WatcherView.helpPage)
    public String helpPage(@ModelAttribute(Sessionable.User) User user, Model model) {
        return WatcherView.helpPage;
    }

    @RequestMapping(value = "/" + WatcherView.activePage)
    public String activePage(@ModelAttribute(Sessionable.User) User user, Model model) {
        model.addAttribute("devices", deviceService.findAllActiveDevices());
        return WatcherView.activePage;
    }
}
