package em.watcher.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportPacketRepository extends CrudRepository<ReportPacket, Long> {
    List<ReportPacket> findById(Long Id);

    Page<ReportPacket> findByDefId(Long defid, Pageable pageable);

    Page<ReportPacket> findByDefIdAndDeviceId(Long defid, Long deviceId, Pageable pageable);

    @Query("select p from ReportPacket p where p.id = (select max(q.id) from ReportPacket q where q.defId = ?1)")
    ReportPacket getLatest(Long defId);

    @Query("select p from ReportPacket p where p.id = (select max(q.id) from ReportPacket q where q.defId = ?1 and q.deviceId = ?2)")
    ReportPacket getLatest(Long defId, Long devId);

    @Query("select p from ReportPacket p where p.id = (select max(q.id) from ReportPacket q where q.deviceId = ?1)")
    ReportPacket getLatestByDev(Long devId);
    @Query("select p from ReportPacket p where p.id = (select max(q.id) from ReportPacket q where q.authId = ?1)")
    ReportPacket getLatestByAuth(Long authId);

}
