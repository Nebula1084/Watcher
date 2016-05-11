package em.watcher.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(ControlDefCache.NAME)
public class ControlDefCache implements ControlDefRepository{
    public final static String NAME = "ControlDefCache";

    @Autowired
    ControlDefRepository controlDefRepository;

    @Override
    public List<ControlDef> findById(Long Id) {
        return controlDefRepository.findById(Id);
    }

    @Override
    public <S extends ControlDef> S save(S entity) {
        return controlDefRepository.save(entity);
    }

    @Override
    public <S extends ControlDef> Iterable<S> save(Iterable<S> entities) {
        return controlDefRepository.save(entities);
    }

    @Override
    public ControlDef findOne(Long aLong) {
        return controlDefRepository.findOne(aLong);
    }

    @Override
    public boolean exists(Long aLong) {
        return controlDefRepository.exists(aLong);
    }

    @Override
    public Iterable<ControlDef> findAll() {
        return controlDefRepository.findAll();
    }

    @Override
    public Iterable<ControlDef> findAll(Iterable<Long> longs) {
        return controlDefRepository.findAll(longs);
    }

    @Override
    public long count() {
        return controlDefRepository.count();
    }

    @Override
    public void delete(Long aLong) {
        controlDefRepository.delete(aLong);
    }

    @Override
    public void delete(ControlDef entity) {
        controlDefRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends ControlDef> entities) {
        controlDefRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        controlDefRepository.deleteAll();
    }
}
