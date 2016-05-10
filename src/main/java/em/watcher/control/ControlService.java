package em.watcher.control;

import em.watcher.PacketValidator;
import em.watcher.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static em.watcher.conroller.PacketController.*;

@Service
public class ControlService {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ControlDefRepository controlDefRepository;

    @Autowired
    private PacketValidator packetValidator;

    public void control(Map<String, String> params) throws Exception {
        Long authId = Long.valueOf(params.get(AUTH_ID));
        String authKey = params.get(AUTH_KEY);
        Long deviceId = Long.valueOf(params.get(DEVICE_ID));
        Long reportId = Long.valueOf(params.get(CONTROL_ID));

        ControlDef controlDef;
        if (!deviceService.authenticate(authId, authKey))
            throw new Exception("Authenticate failed.");
        if (!deviceService.isExist(deviceId))
            throw new Exception("Device " + deviceId + " doesn't exist.");
        controlDef = this.getControlDef(reportId);
        packetValidator.validatePacket(params, controlDef);
        ControlPacket packet = new ControlPacket();
        for (String field : controlDef.getField()) {
            packet.putField(field, params.get(field));
        }
        this.control(controlDef, packet);
    }

    public void control(Long id, ControlPacket controlPacket) throws Exception {
        control(getControlDef(id), controlPacket);
    }

    public void control(ControlDef controlDef, ControlPacket controlPacket) {
        controlDef.addControl(controlPacket);
        controlDefRepository.save(controlDef);
    }

    public ControlDef getControlDef(Long id) throws Exception {
        List<ControlDef> controlDefs = controlDefRepository.findById(id);
        if (controlDefs.isEmpty())
            throw new Exception("Control " + id + " doesn't exist");
        else
            return controlDefs.get(0);
    }
}
