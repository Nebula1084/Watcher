package em.watcher.device;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Device)) return false;
        Device device = (Device) obj;
        return Objects.equals(this.name, device.name);
    }
}
