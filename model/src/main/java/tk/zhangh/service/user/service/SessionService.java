package tk.zhangh.service.user.service;

import tk.zhangh.service.user.domain.model.session.Session;
import tk.zhangh.service.user.domain.model.session.exceptions.NoSuchSessionException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by ZhangHao on 17/8/20.
 */
public interface SessionService {

    Session createSession(Long sessionId, Long userId, String token);

    Session createSession(Long sessionId, Long userId, String token, int minutes);

    Optional<Session> findSession(Long id);

    Session getSession(Long id) throws NoSuchSessionException;

    void logoutUser(Long userId);

    void useSession(Long id, String value, LocalDateTime lastUsedAt) throws NoSuchSessionException;
}
