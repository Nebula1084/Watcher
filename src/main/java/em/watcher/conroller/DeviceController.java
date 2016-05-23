package em.watcher.conroller;

import em.watcher.Sessionable;
import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import em.watcher.control.ControlService;
import em.watcher.device.Device;
import em.watcher.device.DeviceService;
import em.watcher.report.ReportDef;
import em.watcher.report.ReportPacket;
import em.watcher.report.ReportService;
import em.watcher.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static em.watcher.conroller.PacketController.*;
import static em.watcher.control.ControlPacket.Send;

@Controller
@SessionAttributes({Sessionable.Status, Sessionable.User, DeviceController.DEVICE})
public class DeviceController {
    static final String DEVICE = "device";

    @Autowired
    DeviceService deviceService;
    @Autowired
    ControlService controlService;
    @Autowired
    ReportService reportService;
    private static final int itemOfPage = 10;

    @RequestMapping(value = "/device/view/{id}.html")
    public String devicePage(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute(DEVICE, deviceService.findDevice(id));
            return WatcherView.devicePage;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/" + WatcherView.indexPage;
        }
    }

    @RequestMapping(value = "/device/report.do", method = RequestMethod.POST)
    public void report(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            params.put(key, request.getParameter(key));
        }
        params.put(AUTH_KEY, "-1");
        params.put(AUTH_ID, "-1");
        params.put(DEVICE_ID, "-1");
        Device device = (Device) request.getSession().getAttribute(DEVICE);
        try {
            reportService.report(params, false);
            response.sendRedirect("/device/view/" + device.getId() + ".html");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @RequestMapping(value = "/device/control.do", method = RequestMethod.POST)
    public void control(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            params.put(key, request.getParameter(key));
        }
        HttpSession session = request.getSession();
        Device target = (Device) session.getAttribute(DEVICE);
        params.put(AUTH_KEY, "-1");
        params.put(AUTH_ID, "-1");
        params.put(DEVICE_ID, "-1");
        params.put(SR, Send);
        params.put(TARGET_ID, target.getId().toString());
        try {
            controlService.control(params, false);
            response.sendRedirect("/device/view/" + target.getId() + ".html");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping(value = "/device/overview.html")
    public String deviceOverview(@ModelAttribute(DEVICE) Device device, @ModelAttribute(Sessionable.User) User user, Model model) {
        Map<ControlDef, ControlPacket> latestControl = new LinkedHashMap<>();
        Map<ReportDef, ReportPacket> latestReport = new LinkedHashMap<>();
        user.getControlDefs().forEach(controlDef -> {
            ControlPacket latestPacket = controlService.getLatest(controlDef, device);
            if (latestPacket != null)
                latestControl.put(controlDef, latestPacket);
        });
        user.getReportDefs().forEach(reportDef -> {
            ReportPacket latestPacket = reportService.getLatest(reportDef, device);
            if (latestPacket != null)
                latestReport.put(reportDef, latestPacket);
        });
        model.addAttribute("latestControl", latestControl);
        model.addAttribute("latestReport", latestReport);
        if (false) {
            WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
            context.setVariable("latestControl", latestControl);
            context.setVariable("latestReport", latestReport);
        }
        return "/device/overview.html";
    }

    @RequestMapping(value = "/device/send/report/{defId}.html")
    public String sendReport(@PathVariable("defId") Long defId, Model model,
                             @ModelAttribute(DEVICE) Device device) {
        try {
            ReportDef reportDef = reportService.getReportDef(defId);
            model.addAttribute("packetDef", reportDef);
            if (false) {
                WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
                context.setVariable("packetDef", reportDef);
            }
            return "/device/send.html :: send(target='report',id='" + defId + "')";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/device/view/" + device.getId() + ".html";
        }
    }

    @RequestMapping(value = "/device/send/control/{defId}.html")
    public String sendControl(@PathVariable("defId") Long defId, Model model,
                              @ModelAttribute(DEVICE) Device device) {
        try {
            ControlDef controlDef = controlService.getControlDef(defId);
            model.addAttribute("packetDef", controlDef);
            System.out.println(device);
            if (false) {
                WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
                context.setVariable("packetDef", controlDef);
            }
            return "/device/send.html :: send(target='control',id='" + defId + "')";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/device/view/" + device.getId() + ".html";
        }
    }

    @RequestMapping(value = "/device/log/report.html")
    public String reportLog(@RequestParam("defId") Long defId, @ModelAttribute(DEVICE) Device device, Model model,
                            @PageableDefault(value = itemOfPage, sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            ReportDef reportDef = reportService.getReportDef(defId);
            Page<ReportPacket> packets = reportService.getReportPackets(reportDef, device, pageable);
            model.addAttribute("packetDef", reportDef);
            model.addAttribute("packets", packets.getContent());
            if (false) {
                WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
                context.setVariable("packets", packets);
            }
            return "/device/log.html :: log(target='report', page=" + (pageable.getPageNumber() + 1) + ", page_count=" + packets.getTotalPages() + ")";
        } catch (Exception e) {
            e.printStackTrace();
            return "/device/overview.html";
        }
    }

    @RequestMapping(value = "/device/log/control.html")
    public String controlLog(@RequestParam("defId") Long defId, @ModelAttribute(DEVICE) Device device, Model model,
                             @PageableDefault(value = itemOfPage, sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            ControlDef controlDef = controlService.getControlDef(defId);
            Page<ControlPacket> packets = controlService.getControlPackets(controlDef, device, pageable);

            model.addAttribute("packetDef", controlDef);
            model.addAttribute("packets", packets.getContent());
            if (false) {
                WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
                context.setVariable("packets", packets);
            }
            return "/device/log.html :: log(target='control', page=" + (pageable.getPageNumber() + 1) + ", page_count=" + packets.getTotalPages() + ")";
        } catch (Exception e) {
            e.printStackTrace();
            return "/device/overview.html";
        }

    }

    @ModelAttribute("andChar")
    public String and() {
        return "&";
    }

    @ModelAttribute("debug")
    public Boolean debug() {
        Boolean debug=false;
        if (false) {
            WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
            context.setVariable("debug", debug);
        }
        return debug;
    }

}
