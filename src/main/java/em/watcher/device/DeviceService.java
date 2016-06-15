package em.watcher.device;

import em.watcher.WatcherPacket;
import em.watcher.control.ControlPacketRepository;
import em.watcher.report.ReportPacket;
import em.watcher.report.ReportPacketRepository;
import org.h2.store.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        return devices.get(0);
    }

    public List<Device> findAllActiveDevices() {
        List<Device> devices = deviceRepository.findAll();
        Date current = Calendar.getInstance().getTime();
        return devices.stream().filter(device -> {
            if (getInterval(current, reportPacketRepository.getLatestByDev(device.getId())))
                return true;
            if (getInterval(current, reportPacketRepository.getLatestByAuth(device.getId())))
                return true;
            if (getInterval(current, controlPacketRepository.getLatestByDev(device.getId())))
                return true;
            if (getInterval(current, controlPacketRepository.getLatestByAuth(device.getId())))
                return true;
            return false;
        }).collect(Collectors.toList());
    }

    private Boolean getInterval(Date now, WatcherPacket past) {
        if (now == null)
            return false;
        if (past == null)
            return false;
        System.out.println(now);
        System.out.println(past.getDate());
        return now.getTime() - past.getDate().getTime() < 60 * 60 * 1000;
    }

}
