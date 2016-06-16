package em.watcher.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import em.watcher.control.ControlDef;
import em.watcher.control.ControlPacket;
import em.watcher.control.ControlService;
import em.watcher.device.Device;
import em.watcher.device.DeviceService;
import em.watcher.report.ReportDef;
import em.watcher.report.ReportPacket;
import em.watcher.report.ReportService;
import em.watcher.user.ContentMatcher;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static em.watcher.control.ControlPacket.Recv;
import static em.watcher.control.ControlPacket.Send;

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
            Scanner scanner = new Scanner(request.getInputStream());
            String jsonStr = "";
            while (scanner.hasNext()) {
                jsonStr += scanner.next();
            }
            if (Objects.equals(jsonStr, ""))
                reportService.report(getParams(request));
            else
                reportService.report(getParams(jsonStr));
            response.getWriter().println("{\"code\":0}");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            sendError(response, 400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, 403, e.getMessage());
        }
    }

    @RequestMapping(value = "/api/control", method = RequestMethod.POST)
    public void control(HttpServletRequest request, HttpServletResponse response) {
        try {
            ControlPacket ret = controlService.control(getParams(request));
            if (ret == null)
                response.getWriter().print("{\"code\":-1}");
            else {
                if (Objects.equals(ret.getSR(), Send))
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

    @RequestMapping(value = "api/data", method = RequestMethod.GET)
    public DataOut data(@RequestParam("report_id") Long defId, @RequestParam("device_id") Long device_Id,
                        @PageableDefault(value = 200, sort = {"time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReportPacket> packets;
        DataOut dataOut = new DataOut();
        try {
            ReportDef reportDef = reportService.getReportDef(defId);
            packets = reportService.getReportPackets(reportDef.getId(), device_Id, pageable);
            dataOut.data = packets.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataOut;
    }

    @RequestMapping(value = "api/push", method = RequestMethod.POST)
    public void push(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Scanner scanner = new Scanner(request.getInputStream());
            String jsonStr = "";
            while (scanner.hasNext()) {
                jsonStr += scanner.next();
            }
            Map<String, Object> attr = objectMapper.readValue(jsonStr, Map.class);
            Map<String, String> params = new HashMap<>();
            params.put(CONTROL_ID, attr.get(CONTROL_ID).toString());
            params.put(TARGET_ID, request.getParameter(DEVICE_ID).toString());
            params.put(AUTH_KEY, "-1");
            params.put(AUTH_ID, "-1");
            params.put(DEVICE_ID, "-1");
            params.put(SR, Send);
            List<Map<String, Object>> fields = (List<Map<String, Object>>) attr.get("payload");
            for (Map<String, Object> field : fields) {
                params.put(field.get("name").toString(), field.get("value").toString());
            }
            ControlPacket ret = controlService.control(params, false);
            if (ret == null)
                response.getWriter().print("{\"code\":-1}");
            else {
                response.getWriter().print("{\"code\":0}");
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

    @RequestMapping(value = "api/poll", method = RequestMethod.POST)
    public void poll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Scanner scanner = new Scanner(request.getInputStream());
            String jsonStr = "";
            while (scanner.hasNext()) {
                jsonStr += scanner.next();
            }
            Map<String, String> params = getParams(jsonStr);
            params.put(SR, Recv);

            ControlPacket ret = controlService.control(params);
            if (ret == null)
                response.getWriter().print("{\"code\":-1}");
            else {
                ret.getPayload();
                Map<String, Object> res= new HashMap<>();

                ControlDef controlDef = controlService.getControlDef(ret.getDefId());
                for (String key : controlDef.getField()){
                    switch (controlDef.getType(key)){
                        case ControlDef.TYPE_FLOAT:
                            res.put(key, Float.valueOf(ret.getPayload().get(key)));
                            break;
                        case ControlDef.TYPE_INT:
                            res.put(key, Integer.valueOf(ret.getPayload().get(key)));
                            break;
                        default:
                            res.put(key, ret.getPayload().get(key));
                    }
                }
                res.put("code", 0);
                objectMapper.writeValue(response.getWriter(), res);
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

    private Map<String, String> getParams(HttpServletRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();
        Set<String> keys = request.getParameterMap().keySet();
        for (String key : keys) {
            String[] values = request.getParameterValues(key);
            if (values.length != 1) throw new IllegalArgumentException("Parameter format is not correct.");
            if (key.equals("payload")) {
                Map<String, Object> attr = objectMapper.readValue(values[0], Map.class);
                for (String attrKey : attr.keySet())
                    params.put(attrKey, attr.get(attrKey).toString());
            } else
                params.put(key, values[0]);

        }
        return params;
    }

    private Map<String, String> getParams(String request) throws IOException {
        Map<String, String> params = new HashMap<>();
        Map<String, Object> param = objectMapper.readValue(request, Map.class);
        Set<String> keys = param.keySet();
        for (String key : keys) {
            Object values = param.get(key);
            if (key.equals("payload")) {
                Map<String, Object> attr = (Map<String, Object>) values;
                for (String attrKey : attr.keySet())
                    params.put(attrKey, attr.get(attrKey).toString());
            } else
                params.put(key, values.toString());

        }
        return params;
    }
}
