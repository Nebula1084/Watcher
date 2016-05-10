
package em.watcher;

import em.watcher.conroller.PacketController;
import em.watcher.control.ControlDef;
import em.watcher.device.Device;
import em.watcher.device.DeviceRepository;
import em.watcher.device.DeviceService;
import em.watcher.report.ReportDef;
import em.watcher.user.User;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = App.class)
@Rollback
@Transactional
public class ReportTest {
    @Autowired
    PacketController packetController;
    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceRepository deviceRepository;
    private Logger logger = Logger.getLogger(ReportTest.class);
    private MockMvc mockMvc;
    private User user;
    private Device device;
    private ReportDef reportDef;
    private ControlDef controlDef;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.packetController).build();
        this.user = this.userService.register(new User("user111", "12443", "123"));
        Assert.assertThat(this.user != null, CoreMatchers.is(true));
        this.user = this.userService.registerDevice(this.user, new Device("dev1"));
        this.device = this.user.getDevices().get(0);
        Assert.assertThat(this.device != null, CoreMatchers.is(true));
        System.out.println("----------------------------------");
        this.logger.error(this.device);
        System.out.println(this.deviceRepository.findAll());
        System.out.println();
        Assert.assertThat(this.deviceService.isExist(this.device.getId()), CoreMatchers.is(true));
        this.reportDef = new ReportDef("report1");
        this.reportDef.addField("f1", WatcherPacketDef.TYPE_INT, 4);
        this.reportDef.addField("f2", WatcherPacketDef.TYPE_STRING, 10);
        this.user = this.userService.registerReport(this.user, this.reportDef);
        this.reportDef = this.user.getReportDefs().get(0);
        Assert.assertThat(this.reportDef == null, CoreMatchers.not(true));
        this.controlDef = new ControlDef("control1");
        this.controlDef.addField("f1", WatcherPacketDef.TYPE_INT, 4);
        this.controlDef.addField("f2", WatcherPacketDef.TYPE_STRING, 10);
        this.controlDef.addField("f3", WatcherPacketDef.TYPE_CHAR, 1);
        this.user = this.userService.registerControl(this.user, this.controlDef);
        this.controlDef = this.user.getControlDefs().get(0);
    }

    @Test
    public void testReport() throws Exception {
        MultiValueMap<String, String> mvm = this.getMvm(this.device.getId(), reportDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isOk());
        mvm.add("f3", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm = this.getMvm(this.device.getId(), reportDef);
        mvm.add("f3", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm = this.getMvm(100L, reportDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        this.mockMvc.perform(post("/api/report").params(mvm)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void testControl() throws Exception {
        MultiValueMap<String, String> mvm = this.getMvm(this.device.getId(), controlDef);
        mvm.add("f1", "12");
        mvm.add("f2", "sdfasdf");
        mvm.add("f3", "1");
        this.mockMvc.perform(post("/api/control").params(mvm)).andDo(print()).andExpect(status().isBadRequest());
        mvm.add("sr", "S");
        this.mockMvc.perform(post("/api/control").params(mvm)).andDo(print()).andExpect(status().isOk());
    }

    private MultiValueMap<String, String> getMvm(Long id, WatcherPacketDef packetDef) {
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
