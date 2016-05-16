package em.watcher.report;

import em.watcher.WatcherPacketDef;

import javax.persistence.Entity;

@Entity
public class ReportDef extends WatcherPacketDef {
    final static public String CACHE = "ReportDefs";

    public ReportDef() {
        super();
    }

    public ReportDef(String name) {
        super(name);
    }

}
