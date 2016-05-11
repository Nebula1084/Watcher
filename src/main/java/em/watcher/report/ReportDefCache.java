package em.watcher.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(ReportDefCache.NAME)
public class ReportDefCache implements ReportDefRepository {
    public final static String NAME = "ReportDefCache";

    @Autowired
    private ReportDefRepository reportDefRepository;

    @Override
    public List<ReportDef> findById(Long Id) {
        return reportDefRepository.findById(Id);
    }

    @Override
    public <S extends ReportDef> S save(S entity) {
        return reportDefRepository.save(entity);
    }

    @Override
    public <S extends ReportDef> Iterable<S> save(Iterable<S> entities) {
        return reportDefRepository.save(entities);
    }

    @Override
    public ReportDef findOne(Long aLong) {
        return reportDefRepository.findOne(aLong);
    }

    @Override
    public boolean exists(Long aLong) {
        return reportDefRepository.exists(aLong);
    }

    @Override
    public Iterable<ReportDef> findAll() {
        return reportDefRepository.findAll();
    }

    @Override
    public Iterable<ReportDef> findAll(Iterable<Long> longs) {
        return reportDefRepository.findAll(longs);
    }

    @Override
    public long count() {
        return reportDefRepository.count();
    }

    @Override
    public void delete(Long aLong) {
        reportDefRepository.delete(aLong);
    }

    @Override
    public void delete(ReportDef entity) {
        reportDefRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends ReportDef> entities) {
        reportDefRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        reportDefRepository.deleteAll();
    }
}
