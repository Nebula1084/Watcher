package em.watcher.control;

import em.watcher.WatcherPacketDef;
import em.watcher.report.ReportlPacket;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class ControlDef extends WatcherPacketDef {

    final static public String CACHE = "ControlDefs";

    public ControlDef() {
        super();
    }

    public ControlDef(String name) {
        super(name);
    }

}
