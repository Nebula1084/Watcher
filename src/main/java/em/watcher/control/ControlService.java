package em.watcher.control;

import em.watcher.PacketValidator;
import em.watcher.device.Device;
import em.watcher.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private ControlPacketPool packetPool;
    @Autowired
    private ControlPacketRepository packetRepository;

    public ControlPacket control(Map<String, String> params) throws Exception {
        return control(params, true);
    }

    public ControlPacket control(Map<String, String> params, Boolean fromDevice) throws Exception {
        Long authId = Long.valueOf(params.get(AUTH_ID));
        String authKey = params.get(AUTH_KEY);
        Long controlId = Long.valueOf(params.get(CONTROL_ID));

        ControlDef controlDef;
        controlDef = this.getControlDef(controlId);

        if (fromDevice && !deviceService.authenticate(authId, authKey))
            throw new Exception("Authenticate failed.");

        packetValidator.validatePacket(params, controlDef);

        Long deviceId = Long.valueOf(params.get(DEVICE_ID));

        Device device = null;
        if (fromDevice) {
            device = deviceService.findDevice(deviceId);
            deviceService.activate(device);
        }

        ControlPacket packet = new ControlPacket();
        packet.setAuthId(authId);
        packet.setDeviceId(deviceId);
        packet.setSR(params.get(SR));
        if (packet.getSR().equals(ControlPacket.Send))
            for (String field : controlDef.getField()) {
                packet.putField(field, params.get(field));
            }
        packet.setDefId(controlDef.getId());
        switch (packet.getSR()) {
            case ControlPacket.Send:
                packet = this.recordControl(packet);
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

    public ControlPacket sendControl(Device device, ControlPacket packet) {
        packet = packetRepository.save(packet);
        packetPool.offer(device, packet);
        return new ControlPacket();
    }

    public ControlPacket recvControl(Device device, ControlPacket packet) throws InterruptedException {
//        packetRepository.save(packet);
        return packetPool.blockingPoll(device);
    }

    //    public ControlPacket sendControl(Device device, ControlPacket packet) {
//        synchronized (packet) {
//            packetPool.offer(device, packet);
//            try {
//                packet.wait(10000);
//                packetRepository.save(packet);
//                if (Objects.equals(packet.getFellowPacketId(), ControlPacket.NO_FELLOW)) {
//                    packetPool.remove(device, packet);
//                } else {
//                    return packetRepository.findById(packet.getFellowPacketId()).get(0);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
    public ControlPacket recvBlockingControl(Device device, ControlPacket packet) throws InterruptedException {
        return packetPool.blockingPoll(device);
    }


    public ControlPacket recordControl(ControlPacket controlPacket) {
        return packetRepository.save(controlPacket);
    }

    public ControlDef getControlDef(Long id) throws Exception {
        List<ControlDef> controlDefs = controlDefRepository.findById(id);
        if (controlDefs.isEmpty())
            throw new Exception("Control " + id + " doesn't exist");
        else
            return controlDefs.get(0);
    }

    public Page<ControlPacket> getControlPackets(ControlDef controlDef, Pageable pageable) {
        return packetRepository.findByDefId(controlDef.getId(), pageable);
    }

    public Page<ControlPacket> getControlPackets(ControlDef controlDef, Device device, Pageable pageable) {
        return packetRepository.findByDefIdAndDeviceId(controlDef.getId(), device.getId(), pageable);
    }

    public ControlPacket getLatest(ControlDef controlDef) {
        return packetRepository.getLatest(controlDef.getId());
    }

    public ControlPacket getLatest(ControlDef controlDef, Device device) {
        return packetRepository.getLatest(controlDef.getId(), device.getId());
    }
}
