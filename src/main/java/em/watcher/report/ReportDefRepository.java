package em.watcher.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportDefRepository extends CrudRepository<ReportDef, Long> {
    List<ReportDef> findById(Long Id);
}
