package em.watcher.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(ControlPacketCache.NAME)
public class ControlPacketCache implements ControlPacketRepository {
    public final static String NAME = "ControlPacketCache";

    @Autowired
    ControlPacketRepository controlPacketRepository;

    @Override
    public List<ControlPacket> findById(Long Id) {
        return controlPacketRepository.findById(Id);
    }

    @Override
    public <S extends ControlPacket> S save(S entity) {
        return controlPacketRepository.save(entity);
    }

    @Override
    public <S extends ControlPacket> Iterable<S> save(Iterable<S> entities) {
        return controlPacketRepository.save(entities);
    }

    @Override
    public ControlPacket findOne(Long aLong) {
        return controlPacketRepository.findOne(aLong);
    }

    @Override
    public boolean exists(Long aLong) {
        return controlPacketRepository.exists(aLong);
    }

    @Override
    public Iterable<ControlPacket> findAll() {
        return controlPacketRepository.findAll();
    }

    @Override
    public Iterable<ControlPacket> findAll(Iterable<Long> longs) {
        return controlPacketRepository.findAll();
    }

    @Override
    public long count() {
        return controlPacketRepository.count();
    }

    @Override
    public void delete(Long aLong) {
        controlPacketRepository.delete(aLong);
    }

    @Override
    public void delete(ControlPacket entity) {
        controlPacketRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends ControlPacket> entities) {
        controlPacketRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        controlPacketRepository.deleteAll();
    }
}
