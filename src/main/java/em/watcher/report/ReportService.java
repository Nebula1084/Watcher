package em.watcher.report;

import em.watcher.PacketValidator;
import em.watcher.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static em.watcher.conroller.PacketController.*;

@Service
public class ReportService {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ReportDefRepository reportDefRepository;

    @Autowired
    private PacketValidator packetValidator;

    @Autowired
    private ReportPacketRepository packetRepository;

    public void report(Map<String, String> params) throws Exception {
        Long authId = Long.valueOf(params.get(AUTH_ID));
        String authKey = params.get(AUTH_KEY);
        Long deviceId = Long.valueOf(params.get(DEVICE_ID));
        Long reportId = Long.valueOf(params.get(REPORT_ID));

        ReportDef reportDef;
        if (!deviceService.authenticate(authId, authKey))
            throw new Exception("Authenticate failed.");
        if (!deviceService.isExist(deviceId))
            throw new Exception("Device " + deviceId + " doesn't exist.");
        reportDef = this.getReportDef(reportId);
        packetValidator.validatePacket(params, reportDef);
        ReportlPacket packet = new ReportlPacket();
        packet.setAuthId(authId);
        packet.setDeviceId(deviceId);
        packet.setDefId(reportDef.getId());
        for (String field : reportDef.getField()) {
            packet.putField(field, params.get(field));
        }
        this.report(packet);
    }


    public void report(ReportlPacket reportlPacket) {
        packetRepository.save(reportlPacket);
    }

    @Cacheable(ReportDef.CACHE)
    public ReportDef getReportDef(Long id) throws Exception {
        List<ReportDef> reportDefs = reportDefRepository.findById(id);
        if (reportDefs.isEmpty())
            throw new Exception("Report " + id + " doesn't exist.");
        else
            return reportDefs.get(0);
    }
}
