package tk.zhangh.service.user.domain.model.user;

import tk.zhangh.service.user.domain.model.user.exceptions.NoSuchUserException;

import java.util.Optional;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public interface UserRepository {

    void delete(Long userId) throws NoSuchUserException;

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByScreenName(String screenName);

    User save(User user);
}
