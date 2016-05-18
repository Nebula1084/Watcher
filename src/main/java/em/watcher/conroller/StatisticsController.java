package em.watcher.conroller;

import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import em.watcher.control.ControlService;
import em.watcher.report.ReportDef;
import em.watcher.report.ReportPacket;
import em.watcher.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.WebContext;

import java.util.List;

@Controller
public class StatisticsController {

    @Autowired
    ReportService reportService;
    @Autowired
    ControlService controlService;

    @RequestMapping(value = "/statistics/report/{id}.html")
    String reportStatistics(@PathVariable("id") Long id, Model model) {
        try {
            ReportDef reportDef = reportService.getReportDef(id);
            List<ReportPacket> reportPackets = reportService.getReportPackets(reportDef);
            model.addAttribute("reportDef", reportDef);
            if (false) {
                WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
                context.setVariable("reportDef", reportDef);
            }
            return WatcherView.reportStatisticsPage;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/" + WatcherView.indexPage;
        }

    }

    @RequestMapping(value = "/statistics/control/{id}.html")
    String controlStatistics(@PathVariable("id") Long id, Model model) {
        try {
            ControlDef controlDef = controlService.getControlDef(id);
            List<ControlPacket> controlPackets = controlService.getControlPackets(controlDef);
            model.addAttribute("controlDef", controlDef);
            if (false) {
                WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
                context.setVariable("controlDef", controlDef);
            }
            return WatcherView.controlStatisticsPage;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/" + WatcherView.indexPage;
        }
    }
}
