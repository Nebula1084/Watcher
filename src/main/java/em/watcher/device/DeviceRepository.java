package em.watcher.device;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findById(Long Id);
    List<Device> findAll();
}