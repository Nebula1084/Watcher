package em.watcher.control;

import em.watcher.WatcherPacketDef;

import javax.persistence.*;

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
