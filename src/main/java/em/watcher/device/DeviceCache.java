package em.watcher.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(DeviceCache.NAME)
public class DeviceCache implements DeviceRepository {
    public final static String NAME = "DeviceCache";

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public List<Device> findById(Long Id) {
        return deviceRepository.findById(Id);
    }

    @Override
    public <S extends Device> S save(S entity) {
        return deviceRepository.save(entity);
    }

    @Override
    public <S extends Device> Iterable<S> save(Iterable<S> entities) {
        return deviceRepository.save(entities);
    }

    @Override
    public Device findOne(Long aLong) {
        return deviceRepository.findOne(aLong);
    }

    @Override
    public boolean exists(Long aLong) {
        return deviceRepository.exists(aLong);
    }

    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Iterable<Device> findAll(Iterable<Long> longs) {
        return deviceRepository.findAll(longs);
    }

    @Override
    public long count() {
        return deviceRepository.count();
    }

    @Override
    public void delete(Long aLong) {
        deviceRepository.delete(aLong);
    }

    @Override
    public void delete(Device entity) {
        deviceRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends Device> entities) {
        deviceRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        deviceRepository.deleteAll();
    }
}
