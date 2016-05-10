package em.watcher;

import em.watcher.conroller.PacketController;
import em.watcher.control.ControlDef;
import em.watcher.control.ControlService;
import em.watcher.device.Device;
import em.watcher.device.DeviceRepository;
import em.watcher.device.DeviceService;
import em.watcher.report.ReportDef;
import em.watcher.user.User;
import em.watcher.user.UserRepository;
import em.watcher.user.UserService;
import org.apache.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static em.watcher.conroller.PacketController.*;
import static em.watcher.conroller.PacketController.CONTROL_ID;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = App.class)
@Rollback
public abstract class PacketTest {
    @Autowired
    PacketController packetController;
    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    ControlService controlService;
    protected Logger logger = Logger.getLogger(ReportTest.class);
    protected MockMvc mockMvc;
    protected User user;
    protected Device device, target;
    protected ReportDef reportDef;
    protected ControlDef controlDef;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.packetController).build();
        Integer i = 20;
        while (true) {
            try {
                this.user = this.userService.register(new User("user" + i, "12443", "123"));
                break;
            } catch (Exception e) {
                i++;
            }
        }
        assertThat(this.user != null, is(true));
        this.user = this.userService.registerDevice(this.user, new Device("dev1"));
        this.device = this.user.getDevices().get(0);
        this.user = this.userService.registerDevice(this.user, new Device("dev2"));
        this.target = this.user.getDevices().get(1);
        assertThat(this.device != null, is(true));
        System.out.println("----------------------------------");
        this.logger.error(this.device);
        System.out.println(this.deviceRepository.findAll());
        System.out.println();
        assertThat(this.deviceService.isExist(this.device.getId()), is(true));
        this.reportDef = new ReportDef("report1");
        this.reportDef.addField("f1", WatcherPacketDef.TYPE_INT, 4);
        this.reportDef.addField("f2", WatcherPacketDef.TYPE_STRING, 10);
        this.user = this.userService.registerReport(this.user, this.reportDef);
        this.reportDef = this.user.getReportDefs().get(0);
        assertThat(this.reportDef == null, not(true));
        this.controlDef = new ControlDef("control1");
        this.controlDef.addField("f1", WatcherPacketDef.TYPE_INT, 4);
        this.controlDef.addField("f2", WatcherPacketDef.TYPE_STRING, 10);
        this.controlDef.addField("f3", WatcherPacketDef.TYPE_CHAR, 1);
        this.user = this.userService.registerControl(this.user, this.controlDef);
        System.out.println("--------------------------------------------");
        System.out.println(this.user.getControlDefs());
        this.controlDef = this.user.getControlDefs().get(0);
        assertThat(controlService.getControlDef(controlDef.getId()) == null, not(true));
    }

    protected MultiValueMap<String, String> getMvm(Long id, WatcherPacketDef packetDef) {
        LinkedMultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add(AUTH_ID, String.valueOf(id));
        mvm.add(AUTH_KEY, "sdf");
        mvm.add(DEVICE_ID, String.valueOf(id));
        if (packetDef instanceof ReportDef)
            mvm.add(REPORT_ID, String.valueOf(packetDef.getId()));
        else if (packetDef instanceof ControlDef)
            mvm.add(CONTROL_ID, String.valueOf(packetDef.getId()));
        return mvm;
    }

}
