package em.watcher.user;

import em.watcher.control.Control;
import em.watcher.device.Device;
import em.watcher.report.Report;
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
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Transactional
    public User registerDevice(User user, Device device) throws Exception {
        if (user.getDevices().contains(device))
            throw new Exception("Device name duplicate.");
        user.addDevice(device);
        return userRepository.save(user);
    }

    @Transactional
    public User registerControl(User user, Control control) throws Exception {
        if (user.getControls().contains(control))
            throw new Exception("Control name duplicate.");
        user.addControl(control);
        return userRepository.save(user);
    }

    @Transactional
    public User registerReport(User user, Report report) throws Exception {
        if (user.getReports().contains(report))
            throw new Exception("Report name duplicate");
        user.addReport(report);
        return userRepository.save(user);
    }
}
