package em.watcher.conroller;

import em.watcher.control.ControlService;
import em.watcher.device.DeviceService;
import em.watcher.report.ReportService;
import em.watcher.user.ContentMatcher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class PacketController {
    private Logger logger = Logger.getLogger(PacketController.class);

    public final static String AUTH_ID = "auth_id";
    public final static String AUTH_KEY = "auth_key";
    public final static String DEVICE_ID = "device_id";
    public final static String REPORT_ID = "report_id";
    public final static String CONTROL_ID = "control_id";
    public final static String SR = "sr";

    @Autowired
    DeviceService deviceService;
    @Autowired
    ReportService reportService;
    @Autowired
    ControlService controlService;
    @Autowired
    ContentMatcher contentMatcher;

    @RequestMapping(value = "/api/report", method = RequestMethod.POST)
    public void report(HttpServletRequest request, HttpServletResponse response) {
        try {
            reportService.report(getParams(request));
        } catch (IllegalArgumentException e) {
            logger.error(e);
            sendError(response, 400, e);
        } catch (Exception e) {
            logger.error(e);
            sendError(response, 403, e);
        }
    }

    @RequestMapping(value = "/api/control", method = RequestMethod.POST)
    public void control(HttpServletRequest request, HttpServletResponse response) {
        try {
            controlService.control(getParams(request));
        } catch (IllegalArgumentException e) {
            logger.error(e);
            sendError(response, 400, e);
        } catch (Exception e) {
            logger.error(e);
            sendError(response, 403, e);
        }
    }

    private void sendError(HttpServletResponse response, int code, Exception e) {
        try {
            response.sendError(code, e.getMessage());
        } catch (IOException e1) {
            logger.error(e1);
            e1.printStackTrace();
        }
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Set<String> keys = request.getParameterMap().keySet();
        for (String key : keys) {
            String[] values = request.getParameterValues(key);
            if (values.length != 1) throw new IllegalArgumentException("Parameter format is not correct.");
            params.put(key, values[0]);
        }
        return params;
    }
}
