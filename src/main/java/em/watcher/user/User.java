package em.watcher.user;

import em.watcher.control.Control;
import em.watcher.device.Device;
import em.watcher.report.Report;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    private String account = "";
    private String password = "";
    private String fullName = "";

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Device> devices;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Control> controls;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Report> reports;

    public User() {
        this.devices = new HashSet<>();
        this.controls = new HashSet<>();
        this.reports = new HashSet<>();
    }

    public User(String account, String password, String fullName) {
        this();
        this.account = account;
        this.password = password;
        this.fullName = fullName;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public void addDevice(Device device) {
        this.devices.add(device);
    }

    public void addControl(Control control) {
        this.controls.add(control);
    }

    public void addReport(Report report) {
        this.reports.add(report);
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public Set<Control> getControls() {
        return controls;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public String toString() {
        return String.format("{account:%s, password:%s, fullName:%s}", this.account, this.password, this.fullName);
    }
}
