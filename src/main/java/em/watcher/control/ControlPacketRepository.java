package em.watcher.control;

import em.watcher.report.ReportPacket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ControlPacketRepository extends CrudRepository<ControlPacket, Long> {
    List<ControlPacket> findById(Long Id);

    Page<ControlPacket> findByDefId(Long defid, Pageable pageable);

    Page<ControlPacket> findByDefIdAndDeviceId(Long defid, Long deviceId, Pageable pageable);

    @Query("select p from ControlPacket p where p.id = (select max(q.id) from ControlPacket q where q.defId = ?1)")
    ControlPacket getLatest(Long defId);

    @Query("select p from ControlPacket p where p.id = (select max(q.id) from ControlPacket q where q.defId = ?1 and q.deviceId = ?2)")
    ControlPacket getLatest(Long defId, Long devId);
}
