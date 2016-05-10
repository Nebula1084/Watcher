package em.watcher;

import em.watcher.user.User;
import em.watcher.user.UserRepository;
import em.watcher.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginTest extends ManageTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    public LoginTest() {
    }

    @Before
    public void before() throws Exception {
        User user = new User("user12", "123", "123");
        this.userService.register(user);
    }

    @Test
    public void test() throws Exception {
        User user = new User("user12", "123", "123");
        assertThat(this.userService.login(user) != null, is(true));

    }

    @Test(expected = Exception.class)
    public void testPassword() throws Exception {
        User user = new User("user12", "123", "123");
        user.setPassword("124");
        this.userService.login(user);

    }

    @Test(expected = Exception.class)
    public void testAccount() throws Exception {
        User user = new User("user1", "123", "123");
        this.userService.login(user);
    }
}
