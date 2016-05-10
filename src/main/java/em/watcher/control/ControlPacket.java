package em.watcher.control;

import em.watcher.WatcherPacket;

import javax.persistence.Entity;

@Entity
public class ControlPacket extends WatcherPacket {
    private String SR;
    public static final String Send = "S";
    public static final String Recv = "R";

    public ControlPacket() {
        super();
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getSR() {
        return SR;
    }

}
