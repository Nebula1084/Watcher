package em.watcher.report;

import em.watcher.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<User, Long> {
    List<User> findByAccount(String account);
}
