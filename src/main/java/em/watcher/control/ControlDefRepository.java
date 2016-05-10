package em.watcher.control;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ControlDefRepository extends CrudRepository<ControlDef, Long> {
    List<ControlDef> findById(Long Id);
}
