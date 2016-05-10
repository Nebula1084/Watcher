package em.watcher.control;

import em.watcher.WatcherPacketDef;
import em.watcher.report.ReportlPacket;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class ControlDef extends WatcherPacketDef {

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<ControlPacket> packets;

    public ControlDef() {
        super();
        packets = new LinkedList<>();
    }

    public ControlDef(String name) {
        super(name);
        packets = new LinkedList<>();
    }

    public void addControl(ControlPacket packet) {
        packets.add(packet);
    }

    public ControlPacket getLast() {
        return packets.get(packets.size() - 1);
    }
}
