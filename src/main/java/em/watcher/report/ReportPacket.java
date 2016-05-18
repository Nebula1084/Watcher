package em.watcher.report;

import em.watcher.WatcherPacket;
import em.watcher.WatcherPacketDef;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class ReportPacket extends WatcherPacket {
    final static public String CACHE = "ReportlPackets";
    public ReportPacket() {
        super();
    }

}
