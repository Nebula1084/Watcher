package em.watcher.control;

import em.watcher.PacketValidator;
import em.watcher.device.Device;
import em.watcher.device.DeviceService;
import em.watcher.user.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static em.watcher.conroller.PacketController.*;

@Service
public class ControlService {
    @Autowired
    private DeviceService deviceService;
    @Resource(name = ControlDefCache.NAME)
    private ControlDefRepository controlDefRepository;
    @Autowired
    private PacketValidator packetValidator;
    @Autowired
    private ControlPacketPool packetPool;
    @Resource(name = ControlPacketCache.NAME)
    private ControlPacketRepository packetRepository;


    public ControlPacket control(Map<String, String> params) throws Exception {
        Long authId = Long.valueOf(params.get(AUTH_ID));
        String authKey = params.get(AUTH_KEY);
        Long controlId = Long.valueOf(params.get(CONTROL_ID));

        ControlDef controlDef;
        controlDef = this.getControlDef(controlId);

        if (!deviceService.authenticate(authId, authKey))
            throw new Exception("Authenticate failed.");

        packetValidator.validatePacket(params, controlDef);

        Long deviceId = Long.valueOf(params.get(DEVICE_ID));
        Device device = deviceService.findDevice(deviceId);

        ControlPacket packet = new ControlPacket();
        packet.setAuthId(authId);
        packet.setDeviceId(deviceId);
        packet.setSR(params.get(SR));
        if (packet.getSR().equals(ControlPacket.Send))
            for (String field : controlDef.getField()) {
                packet.putField(field, params.get(field));
            }
        packet = this.recordControl(controlDef, packet);
        switch (packet.getSR()) {
            case ControlPacket.Send:
                Long targetId = Long.valueOf(params.get(TARGET_ID));
                Device target = deviceService.findDevice(targetId);
                packet.setTargetId(targetId);
                return sendControl(target, packet);
            case ControlPacket.Recv:
                return recvControl(device, packet);
            default:
                return null;
        }
    }

    private ControlPacket sendControl(Device device, ControlPacket packet) {
        synchronized (packet) {
            packetPool.offer(device, packet);
            try {
                packet.wait(10000);
                packetRepository.save(packet);
                if (Objects.equals(packet.getFellowPacketId(), ControlPacket.NO_FELLOW)) {
                    packetPool.remove(device, packet);
                } else {
                    return packetRepository.findById(packet.getFellowPacketId()).get(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ControlPacket recvControl(Device device, ControlPacket packet) {
        ControlPacket recvPacket = packetPool.poll(device);
        if (recvPacket == null)
            return null;
        synchronized (recvPacket) {
            recvPacket.setFellowPacketId(packet.getId());
            packet.setFellowPacketId(recvPacket.getId());
            packetRepository.save(packet);
            recvPacket.notify();
        }
        return recvPacket;
    }

    public void recordControl(Long id, ControlPacket controlPacket) throws Exception {
        recordControl(getControlDef(id), controlPacket);
    }

    public ControlPacket recordControl(ControlDef controlDef, ControlPacket controlPacket) {
        controlDef.addControl(controlPacket);
        controlDef = controlDefRepository.save(controlDef);
        return controlDef.getLast();
    }

    public ControlDef getControlDef(Long id) throws Exception {
        List<ControlDef> controlDefs = controlDefRepository.findById(id);
        if (controlDefs.isEmpty())
            throw new Exception("Control " + id + " doesn't exist");
        else
            return controlDefs.get(0);
    }
}
