package em.watcher.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service("userService")
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserService() {
    }

    @Transactional
    public User register(String account, String password, String nickName) {
        User user = new User(account, password, nickName);
        return this.register(user);
    }

    @Transactional
    public User register(User user) {
        List<User> users = this.userRepository.findByAccount(user.getAccount());
        if (users.isEmpty()) {
            this.userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    @Transactional
    public User login(User user) {
        List<User> users = this.userRepository.findByAccount(user.getAccount());
        if (!users.isEmpty() && Objects.equals(user.getPassword(), users.get(0).getPassword()))
            return users.get(0);
        else
            return null;
    }

    @Transactional
    public void save(User user) {
        this.userRepository.save(user);
    }
}
