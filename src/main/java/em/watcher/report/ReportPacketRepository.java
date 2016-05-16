package em.watcher.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportPacketRepository extends CrudRepository<ReportlPacket, Long>{
    List<ReportlPacket> findById(Long Id);
}
