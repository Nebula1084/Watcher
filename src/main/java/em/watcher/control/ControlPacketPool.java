package em.watcher.control;

import em.watcher.device.Device;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ControlPacketPool {
    private Map<Device, Deque<ControlPacket>> pool;

    public ControlPacketPool() {
        pool = Collections.synchronizedMap(new HashMap<>());
    }

    public void offer(Device device, ControlPacket controlPacket) {
        Deque<ControlPacket> packetDeque = pool.get(device);
        if (packetDeque == null) {
            synchronized (device) {
                if (packetDeque == null)
                    packetDeque = new LinkedBlockingDeque<>();
            }
        }
        packetDeque.offer(controlPacket);
    }

    public ControlPacket poll(Device device) {
        Deque<ControlPacket> packetDeque = pool.get(device);
        if (packetDeque == null)
            return null;
        return pool.get(device).poll();
    }

    public void remove(Device device, ControlPacket controlPacket) {
        Deque<ControlPacket> packetDeque = pool.get(device);
        if (packetDeque == null) return;
        packetDeque.remove(controlPacket);
    }
}
