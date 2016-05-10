package em.watcher.report;

import em.watcher.WatcherPacketDef;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class ReportDef extends WatcherPacketDef {

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<ReportlPacket> packets;

    public ReportDef() {
        super();
        packets=new LinkedList<>();
    }

    public ReportDef(String name) {
        super(name);
        packets=new LinkedList<>();
    }

    public void addReport(ReportlPacket packet){
        packets.add(packet);
    }

}
