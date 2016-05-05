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
    public void before() {
        User user = new User("user", "123", "123");
        this.userService.register(user);
    }

    @Test
    public void test() {
        User user = new User("user", "123", "123");
        assertThat(this.userService.login(user) != null, is(true));
        user.setPassword("124");
        assertThat(this.userService.login(user) != null, is(false));
        user = new User("user1", "123", "123");
        assertThat(this.userService.login(user) != null, is(false));
    }
}
