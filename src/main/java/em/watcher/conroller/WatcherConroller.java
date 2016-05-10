package em.watcher.conroller;

import em.watcher.Sessionable;
import em.watcher.WatcherPacketDef;
import em.watcher.WatcherStatus;
import em.watcher.control.ControlDef;
import em.watcher.device.Device;
import em.watcher.report.ReportDef;
import em.watcher.user.PassMatcher;
import em.watcher.user.User;
import em.watcher.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

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
    public String deviceRegister(@RequestParam(value = "name_fields") String device_name,
                                 @ModelAttribute(Sessionable.User) User user, Model model,
                                 @ModelAttribute(Sessionable.Status) WatcherStatus status) {
        Device device;
        try {
            if (!passMatcher.validate(device_name))
                throw new Exception("Device name contains invalid character.");
            device = new Device(device_name);
            model.addAttribute(Sessionable.User, userService.registerDevice(user, device));
            return "redirect:" + WatcherView.indexPage;
        } catch (Exception e) {
            status.setException(e);
            return "redirect:" + WatcherView.deviceRgstPage;
        }
    }

    @RequestMapping(value = "/reportDef/register.do", method = RequestMethod.POST)
    public String reportRegister(HttpServletRequest httpRequest, @ModelAttribute(Sessionable.User) User user, Model model,
                                 @ModelAttribute(Sessionable.Status) WatcherStatus status) {
        ReportDef reportDef;
        try {
            reportDef = getReport(httpRequest);
            model.addAttribute(Sessionable.User, userService.registerReport(user, reportDef));
            return "redirect:" + WatcherView.indexPage;
        } catch (Exception e) {
            status.setException(e);
            return "redirect:" + WatcherView.reportRgstPage;
        }
    }

    @RequestMapping(value = "/controlDef/register.do", method = RequestMethod.POST)
    public String controlRegister(HttpServletRequest httpRequest, @ModelAttribute(Sessionable.User) User user, Model model,
                                  @ModelAttribute(Sessionable.Status) WatcherStatus status) {
        ControlDef controlDef;
        try {
            controlDef = getControl(httpRequest);
            model.addAttribute(Sessionable.User, userService.registerControl(user, controlDef));
            return "redirect:" + WatcherView.indexPage;
        } catch (Exception e) {
            status.setException(e);
            return "redirect:" + WatcherView.controlRgstPage;
        }
    }

    private ControlDef getControl(HttpServletRequest request) throws Exception {
        ControlDef controlDef = new ControlDef();
        return (ControlDef) getPacket(request, controlDef);
    }

    private ReportDef getReport(HttpServletRequest request) throws Exception {
        ReportDef reportDef = new ReportDef();
        return (ReportDef) getPacket(request, reportDef);
    }

    private WatcherPacketDef getPacket(HttpServletRequest request, WatcherPacketDef packet) throws Exception {
        String name_fields = request.getParameter("name_fields");
        Set<String> nameSet = new HashSet<>();
        if (!passMatcher.validate(name_fields)) throw new Exception("name contains invalid character.");
        packet.setName(name_fields);
        int paraNum = (request.getParameterMap().size() - 1) / 3;
        String name, type, length;
        for (int i = 1; i <= paraNum; i++) {
            name = "name" + i;
            type = "type" + i;
            length = "length" + i;
            name = request.getParameter(name);
            if (!passMatcher.validate(name)) throw new Exception("field names contains invalid character.");
            if (nameSet.contains(name)) throw new Exception("field names duplicate.");
            nameSet.add(name);
            type = request.getParameter(type);
            if (!passMatcher.validate(type)) throw new Exception("field types contains invalid character.");
            length = request.getParameter(length);
            packet.addField(name, type, Integer.valueOf(length));
        }
        return packet;
    }
}
