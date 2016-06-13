package em.watcher.control;

import em.watcher.WatcherPacket;
import em.watcher.device.Device;
import em.watcher.report.ReportDef;

import javax.persistence.*;

@Entity
public class ControlPacket extends WatcherPacket {
    @Transient
    private Integer code = 0;
    private String SR = "N";
    private Long targetId = NO_TARGET;

    public static final String Send = "S";
    public static final String Recv = "R";
    public static final Long NO_TARGET = -1L;

    public ControlPacket() {
        super();
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getSR() {
        return SR;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetId() {
        return targetId;
    }

}
