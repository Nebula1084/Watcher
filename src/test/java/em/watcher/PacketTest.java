package em.watcher;

import em.watcher.control.ControlDef;
import em.watcher.report.ReportDef;
import em.watcher.user.User;
import em.watcher.user.UserRepository;
import em.watcher.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PacketTest extends ManageTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void test() throws Exception {
        User user = new User("user", "123", "123");
        this.userService.register(user);
        ControlDef controlDef = new ControlDef("c1");
        controlDef.addField("f1", WatcherPacketDef.TYPE_INT, 4);
        controlDef.addField("f2", WatcherPacketDef.TYPE_FLOAT, 4);

        ReportDef reportDef = new ReportDef("r1");
        reportDef.addField("f1", WatcherPacketDef.TYPE_INT, 4);
        reportDef.addField("f2", WatcherPacketDef.TYPE_FLOAT, 4);
    }

    @Test(expected = Exception.class)
    public void testCollision() throws Exception {
        User user = new User("user", "123", "123");
        this.userService.register(user);
        ControlDef controlDef = new ControlDef("c1");
        controlDef.addField("f1", WatcherPacketDef.TYPE_INT, 4);
        controlDef.addField("f1", WatcherPacketDef.TYPE_FLOAT, 4);
    }

}
