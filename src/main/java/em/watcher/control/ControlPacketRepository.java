package em.watcher.control;

import em.watcher.report.ReportPacket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ControlPacketRepository extends CrudRepository<ControlPacket, Long> {
    List<ControlPacket> findById(Long Id);

    List<ControlPacket> findByDefIdOrderByTime(Long defid);
}
