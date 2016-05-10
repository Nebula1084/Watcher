package em.watcher.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import em.watcher.control.ControlPacket;
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
    private ObjectMapper objectMapper = new ObjectMapper();

    public final static String AUTH_ID = "auth_id";
    public final static String AUTH_KEY = "auth_key";
    public final static String DEVICE_ID = "device_id";
    public final static String REPORT_ID = "report_id";
    public final static String CONTROL_ID = "control_id";
    public final static String SR = "sr";
    public final static String TARGET_ID = "target_id";

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
            sendError(response, 400, e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            sendError(response, 403, e.getMessage());
        }
    }

    @RequestMapping(value = "/api/control", method = RequestMethod.POST)
    public void control(HttpServletRequest request, HttpServletResponse response) {
        try {
            ControlPacket ret = controlService.control(getParams(request));
            if (ret == null)
                sendError(response, 404, "Target is not available.");
            else {
                objectMapper.writeValue(response.getWriter(), ret);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error(e);
            sendError(response, 400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            sendError(response, 403, e.getMessage());
        }
    }

    private void sendError(HttpServletResponse response, int code, String m) {
        try {
            response.sendError(code, m);
        } catch (IOException e1) {
            e1.printStackTrace();
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
