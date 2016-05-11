package em.watcher.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(UserCache.NAME)
public class UserCache implements UserRepository {

    public final static String NAME="UserCache";

    @Autowired
    UserRepository userRepository;

    @Override
    public <S extends User> S save(S entity) {
        System.out.println("UserCache::save()");
        return userRepository.save(entity);
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> entities) {
        System.out.println("UserCache::save()");
        return userRepository.save(entities);
    }

    @Override
    public User findOne(Long aLong) {
        return userRepository.findOne(aLong);
    }

    @Override
    public boolean exists(Long aLong) {
        return userRepository.exists(aLong);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> findAll(Iterable<Long> longs) {
        return userRepository.findAll(longs);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void delete(Long aLong) {
        userRepository.delete(aLong);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends User> entities) {
        userRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public List<User> findByAccount(String account) {
        return userRepository.findByAccount(account);
    }
}
