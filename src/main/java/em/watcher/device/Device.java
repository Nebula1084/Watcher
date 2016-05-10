package em.watcher.device;

import em.watcher.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private User user;

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

    public void setUser(User user) {
        this.user = user;
    }

    public boolean authenticate(String key) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Device)) return false;
        Device device = (Device) obj;
        return Objects.equals(this.name, device.name);
    }

    @Override
    public String toString() {
        return String.format("{id : %s, name : %s, user : %s}", id, name, user);
    }
}
