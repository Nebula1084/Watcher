package em.watcher.conroller;

import em.watcher.Sessionable;
import em.watcher.WatcherPacket;
import em.watcher.control.Control;
import em.watcher.device.Device;
import em.watcher.report.Report;
import em.watcher.user.PassMatcher;
import em.watcher.user.User;
import em.watcher.user.UserService;
import org.jboss.logging.annotations.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WatcherConroller implements Sessionable {

    @Autowired
    PassMatcher passMatcher;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String indexPage(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping(value = "/device/register.do", method = RequestMethod.POST)
    public String deviceRegister(@RequestParam(value = "device_name") String device_name, @ModelAttribute(Sessionable.User) User user) {

        if (passMatcher.validate(device_name)){
            Device device=new Device(device_name);
            userService.registerDevice(user,device);
        }
        return "redirect:" + WatcherView.indexPage;
    }

    @RequestMapping(value = "/report/register.do", method = RequestMethod.POST)
    public String reportRegister(HttpServletRequest httpRequest, @ModelAttribute(Sessionable.User) User user) {
        Report report = getReport(httpRequest);
        if (report != null)
            userService.registerReport(user, report);
        return "redirect:" + WatcherView.indexPage;
    }

    @RequestMapping(value = "/control/register.do", method = RequestMethod.POST)
    public String controlRegister(HttpServletRequest httpRequest, @ModelAttribute(Sessionable.User) User user) {
        Control control = getControl(httpRequest);
        if (control != null)
            userService.registerControl(user, control);
        return "redirect:" + WatcherView.indexPage;
    }

    private Control getControl(HttpServletRequest request) {
        Control control = new Control();
        return (Control) getPacket(request, control);
    }

    private Report getReport(HttpServletRequest request) {
        Report report = new Report();
        return (Report) getPacket(request, report);
    }

    private WatcherPacket getPacket(HttpServletRequest request, WatcherPacket packet) {
        String name_fields = request.getParameter("name_fields");
        if (!passMatcher.validate(name_fields)) return null;
        packet.setName(name_fields);
        int paraNum = (request.getParameterMap().size() - 1) / 3;
        String name, type, length;
        for (int i = 1; i <= paraNum; i++) {
            name = "name" + i;
            type = "type" + i;
            length = "length" + i;
            name = request.getParameter(name);
            if (!passMatcher.validate(name)) return null;
            type = request.getParameter(type);
            if (!passMatcher.validate(type)) return null;
            length = request.getParameter(length);
            try {
                packet.addField(name, type, Integer.valueOf(length));
            } catch (Exception e) {
                return null;
            }
        }
        return packet;
    }
}
