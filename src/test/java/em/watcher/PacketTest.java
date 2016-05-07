package em.watcher;

import em.watcher.control.Control;
import em.watcher.device.Device;
import em.watcher.report.Report;
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
        Control control = new Control("c1");
        control.addField("f1", WatcherPacket.TYPE_INT, 4);
        control.addField("f2", WatcherPacket.TYPE_FLOAT, 4);

        Report report = new Report("r1");
        report.addField("f1", WatcherPacket.TYPE_INT, 4);
        report.addField("f2", WatcherPacket.TYPE_FLOAT, 4);
    }

    @Test(expected = Exception.class)
    public void testCollision() throws Exception {
        User user = new User("user", "123", "123");
        this.userService.register(user);
        Control control = new Control("c1");
        control.addField("f1", WatcherPacket.TYPE_INT, 4);
        control.addField("f1", WatcherPacket.TYPE_FLOAT, 4);
    }

}
