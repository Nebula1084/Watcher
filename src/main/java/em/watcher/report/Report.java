package em.watcher.report;

import em.watcher.WatcherPacket;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "report")
public class Report extends WatcherPacket {
    public Report() {
        super();
    }

    public Report(String name) {
        super(name);
    }
}
