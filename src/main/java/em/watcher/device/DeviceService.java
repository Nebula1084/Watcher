package em.watcher.device;

import em.watcher.WatcherPacket;
import em.watcher.control.ControlPacketRepository;
import em.watcher.report.ReportPacket;
import em.watcher.report.ReportPacketRepository;
import org.h2.store.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    ReportPacketRepository reportPacketRepository;
    @Autowired
    ControlPacketRepository controlPacketRepository;
    private Map<Long, Device> deviceMap=new HashMap<>();

    public boolean authenticate(Long id, String key) throws Exception {
        Device device = findDevice(id);
        return device.authenticate(key);
    }

    public boolean isExist(Long id) {
        List<Device> devices = deviceRepository.findById(id);
        return !devices.isEmpty();
    }

    @Cacheable(Device.CACHE)
    public Device findDevice(Long id) throws Exception {
        List<Device> devices = deviceRepository.findById(id);
        if (devices.isEmpty())
            throw new Exception("Device " + id + " doesn't exist.");
        Device device = devices.get(0);
        device.setTimestamp(Calendar.getInstance().getTime());
        deviceMap.put(device.getId(), device);
        return device;
    }

    public List<Device> findAllActiveDevices() {
        List<Device> devices = deviceRepository.findAll();
        Date current = Calendar.getInstance().getTime();
        return devices.stream().filter(device -> getInterval(current, deviceMap.get(device.getId()))).collect(Collectors.toList());
    }

    private Boolean getInterval(Date now, Device device) {
        if (now == null)
            return false;
        if (device == null)
            return false;
        if (device.getTimestamp() == null)
            return false;
        return now.getTime() - device.getTimestamp().getTime() < 60 * 60 * 1000;
    }

}
