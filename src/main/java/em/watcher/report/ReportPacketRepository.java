package em.watcher.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportPacketRepository extends CrudRepository<ReportPacket, Long> {
    List<ReportPacket> findById(Long Id);

    List<ReportPacket> findByDefIdOrderByTime(Long defid);

}
