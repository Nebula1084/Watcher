package em.watcher.control;

import em.watcher.device.Device;
import em.watcher.mouse.Mouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ControlPacketPool {
    private Map<Device, Deque<ControlPacket>> pool;
    final private static long checkInterval = 1000000L;

    public ControlPacketPool() {
        pool = Collections.synchronizedMap(new HashMap<>());
        new Thread(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Thread.sleep(checkInterval);
                    synchronized (this) {
                        Date currentDate = Calendar.getInstance().getTime();
                        pool.forEach((device, deque) -> {
                            while (true) {
                                ControlPacket packet = deque.peek();
                                if (packet == null) break;
                                if (currentDate.getTime() - packet.getDate().getTime() > checkInterval)
                                    deque.poll();
                                else
                                    break;
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void offer(Device device, ControlPacket controlPacket) {
        Deque<ControlPacket> packetDeque = getDeque(device);
        synchronized (packetDeque) {
            packetDeque.offer(controlPacket);
            packetDeque.notify();
        }
    }

    public ControlPacket poll(Device device) {
        Deque<ControlPacket> packetDeque = pool.get(device);
        if (packetDeque == null)
            return null;
        return pool.get(device).poll();
    }

    public ControlPacket blockingPoll(Device device) throws InterruptedException {
        Deque<ControlPacket> packetDeque = getDeque(device);
        ControlPacket controlPacket = null;
        synchronized (packetDeque) {
            controlPacket = packetDeque.poll();
            if (controlPacket == null) {
                packetDeque.wait(1000 * 60 * 2);
                controlPacket = packetDeque.poll();
            }
        }
        return controlPacket;
    }

    public void remove(Device device, ControlPacket controlPacket) {
        Deque<ControlPacket> packetDeque = pool.get(device);
        if (packetDeque == null) return;
        packetDeque.remove(controlPacket);
    }

    private Deque<ControlPacket> getDeque(Device device) {
        Deque<ControlPacket> packetDeque = pool.get(device);
        if (packetDeque == null) {
            synchronized (this) {
                if (packetDeque == null) {
                    packetDeque = new LinkedBlockingDeque<>();
                    pool.put(device, packetDeque);
                }
            }
        }
        return packetDeque;
    }
}
