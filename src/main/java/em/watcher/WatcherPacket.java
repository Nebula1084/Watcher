package em.watcher;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@MappedSuperclass
abstract public class WatcherPacket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long defId;
    private Long authId;
    private Long deviceId;
    private Date time;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> payload;


    public WatcherPacket() {
        time = Calendar.getInstance().getTime();
        payload = new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    public void setDefId(Long defId) {
        this.defId = defId;
    }

    public Long getDefId() {
        return defId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void putField(String key, Object value) {
        payload.put(key, value.toString());
    }

    public String getField(String key) {
        return payload.get(key);
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(time);
    }

    public Date getDate() {
        return time;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WatcherPacket)) return false;
        WatcherPacket packet = (WatcherPacket) obj;
        return Objects.equals(this.id, packet.id);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }
}
