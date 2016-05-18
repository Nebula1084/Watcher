package em.watcher.user;

import em.watcher.control.ControlDef;
import em.watcher.device.Device;
import em.watcher.report.ReportDef;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserService() {
    }

    @Transactional
    public User register(String account, String password, String nickName) throws Exception {
        User user = new User(account, password, nickName);
        return this.register(user);
    }

    @Transactional
    public User register(User user) throws Exception {
        List<User> users = this.userRepository.findByAccount(user.getAccount());
        if (!users.isEmpty()) {
            System.out.println(users);
            throw new Exception("Account " + user.getAccount() + " already exists.");
        }

        this.userRepository.save(user);
        return user;
    }

    @Transactional
    public User login(User user) throws Exception {
        List<User> users = this.userRepository.findByAccount(user.getAccount());
        if (users.isEmpty())
            throw new Exception("Account " + user.getAccount() + " doesn't exists.");
        if (!Objects.equals(user.getPassword(), users.get(0).getPassword()))
            throw new Exception("Password isn't correct");
        return users.get(0);
    }

    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Transactional
    public User registerDevice(User user, Device device) throws Exception {
        if (user.getDevices().stream().filter(device1 -> Objects.equals(device.getName(), device1.getName())).count() > 0)
            throw new Exception("Device name duplicate.");
        user.addDevice(device);
        return userRepository.save(user);
    }

    @Transactional
    public User registerControl(User user, ControlDef controlDef) throws Exception {
        if (user.getReportDefs().stream().filter(controlDef1 -> Objects.equals(controlDef.getName(), controlDef1.getName())).count() > 0)
            throw new Exception("ControlDef name duplicate.");
        user.addControl(controlDef);
        return userRepository.save(user);
    }

    @Transactional
    public User registerReport(User user, ReportDef reportDef) throws Exception {
        if (user.getReportDefs().stream().filter(reportDef1 -> Objects.equals(reportDef.getName(), reportDef1.getName())).count() > 0)
            throw new Exception("ReportDef name duplicate.");
        user.addReport(reportDef);
        return userRepository.save(user);
    }
}
