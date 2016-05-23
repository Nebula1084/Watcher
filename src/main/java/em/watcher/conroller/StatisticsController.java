package em.watcher.conroller;

import em.watcher.Sessionable;
import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import em.watcher.control.ControlService;
import em.watcher.report.ReportDef;
import em.watcher.report.ReportPacket;
import em.watcher.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.WebContext;

import java.util.List;

@Controller
public class StatisticsController implements Sessionable {

    @Autowired
    ReportService reportService;
    @Autowired
    ControlService controlService;
    private static final int itemOfPage = 10;

    @RequestMapping(value = "/statistics/report.html")
    String reportStatistics(@RequestParam("defId") Long defId, Model model,
                            @PageableDefault(value = itemOfPage, sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            ReportDef reportDef = reportService.getReportDef(defId);
            Page<ReportPacket> packets = reportService.getReportPackets(reportDef, pageable);
            model.addAttribute("packetDef", reportDef);
            model.addAttribute("packets", packets.getContent());
            return "statistics.html:: statistics(target='report', page=" + (pageable.getPageNumber() + 1) + ", page_count=" + packets.getTotalPages() + ")";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/" + WatcherView.indexPage;
        }

    }

    @RequestMapping(value = "/statistics/control.html")
    String controlStatistics(@RequestParam("defId") Long defId, Model model,
                             @PageableDefault(value = itemOfPage, sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            ControlDef controlDef = controlService.getControlDef(defId);
            Page<ControlPacket> packets = controlService.getControlPackets(controlDef, pageable);
            model.addAttribute("packetDef", controlDef);
            model.addAttribute("packets", packets.getContent());
            return "statistics.html:: statistics(target='control', page=" + (pageable.getPageNumber() + 1) + ", page_count=" + packets.getTotalPages() + ")";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/" + WatcherView.indexPage;
        }
    }

    @ModelAttribute("andChar")
    public String and() {
        return "&";
    }
}
