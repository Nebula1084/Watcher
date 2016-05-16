package em.watcher.device;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Device {

    final static public String CACHE = "Devices";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userAccount;
    private String name;

    public Device() {

    }

    public Device(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public boolean authenticate(String key) {
        return true;
    }

    public String getKey() {
        return "key";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Device)) return false;
        Device device = (Device) obj;
        return Objects.equals(this.id, device.id);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }

    @Override
    public String toString() {
        return String.format("{id : %s, name : %s, user : %s}", id, name, userAccount);
    }
}
