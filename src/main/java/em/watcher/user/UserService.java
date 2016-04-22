package em.watcher.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserService() {
        System.out.println("UserService::UserService()");
    }

    @Transactional
    public void register(String account, String password, String nickName) {
        User user = new User(account, password, nickName);
        userRepository.save(user);
    }

}
