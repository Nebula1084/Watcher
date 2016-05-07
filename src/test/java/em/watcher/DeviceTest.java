package em.watcher;

import em.watcher.device.Device;
import em.watcher.user.User;
import em.watcher.user.UserRepository;
import em.watcher.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceTest extends ManageTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Before
    public void before() {

    }

    @Test
    public void test() {
        User user = new User("user", "123", "123");
        this.userService.register(user);
        Device device = new Device("test");
        userService.registerDevice(user, device);
    }
}
