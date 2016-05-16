package em.watcher.user;

import em.watcher.control.ControlDef;
import em.watcher.device.Device;
import em.watcher.report.ReportDef;

import javax.persistence.*;
import java.util.*;

@Entity
public class User {
    @Id
    private String account = "";
    private String password = "";
    private String fullName = "";

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Device> devices;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<ControlDef> controlDefs;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<ReportDef> reportDefs;

    public User() {
        this.devices = new LinkedList<>();
        this.controlDefs = new LinkedList<>();
        this.reportDefs = new LinkedList<>();
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
        device.setUserAccount(this.getAccount());
        this.devices.add(device);
    }

    public void addControl(ControlDef controlDef) {
        controlDef.setUserAccount(this.getAccount());
        this.controlDefs.add(controlDef);
    }

    public void addReport(ReportDef reportDef) {
        reportDef.setUserAccount(this.getAccount());
        this.reportDefs.add(reportDef);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<ControlDef> getControlDefs() {
        return controlDefs;
    }

    public List<ReportDef> getReportDefs() {

        return reportDefs;
    }

    public String toString() {
        return String.format("{account:%s, password:%s, fullName:%s}", this.account, this.password, this.fullName);
    }
}
