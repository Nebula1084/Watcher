package em.watcher.report;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportPacketRepository extends CrudRepository<ReportlPacket, Long> {
//    @Cacheable(ReportlPacket.CACHE)
    List<ReportlPacket> findById(Long Id);

//    @Cacheable(ReportlPacket.CACHE)
    <S extends ReportlPacket> S save(S entity);
}
