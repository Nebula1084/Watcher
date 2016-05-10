package em.watcher.control;

import em.watcher.WatcherPacket;
import em.watcher.device.Device;
import em.watcher.report.ReportDef;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class ControlPacket extends WatcherPacket {
    private String SR;
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ControlDef packetDef;
    private Long targetId;
    private Long fellowPacketId = NO_FELLOW;

    public static final String Send = "S";
    public static final String Recv = "R";
    public static final Long NO_FELLOW = -1L;

    public ControlPacket() {
        super();
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getSR() {
        return SR;
    }

    public void setPacketDef(ControlDef packetDef) {
        this.packetDef = packetDef;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setFellowPacketId(Long fellowPacketId) {
        this.fellowPacketId = fellowPacketId;
    }

    public Long getFellowPacketId() {
        return fellowPacketId;
    }
}
