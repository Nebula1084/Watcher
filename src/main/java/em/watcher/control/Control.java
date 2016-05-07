package em.watcher.control;

import em.watcher.WatcherPacket;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "control")
public class Control extends WatcherPacket{
    public Control(){
        super();
    }

    public Control(String name){
        super(name);
    }
}
