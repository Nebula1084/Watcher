package em.watcher.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    public boolean authenticate(Long id, String key) throws Exception {
        List<Device> devices = deviceRepository.findById(id);
        if (devices.isEmpty())
            throw new Exception("Device " + id + " doesn;t exist.");
        Device device = devices.get(0);
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

}
