package em.watcher;

import em.watcher.user.User;
import em.watcher.user.UserRepository;
import em.watcher.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RegisterTest extends ManageTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    public RegisterTest() {
    }

    @Before
    public void before() {
        User user = new User("user", "123", "123");
        this.userService.register(user);
    }

    @Test
    public void test() {
        User user = new User("user1", "123", "123");
        assertThat(this.userService.register(user) != null, is(true));
        user = new User("user1", "123", "123");
        assertThat(this.userService.register(user) != null, is(false));
    }

}
