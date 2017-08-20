package tk.zhangh.service.user.service;

import lombok.extern.slf4j.Slf4j;
import tk.zhangh.service.user.domain.model.session.Session;
import tk.zhangh.service.user.domain.model.session.SessionRepository;
import tk.zhangh.service.user.domain.model.session.exceptions.NoSuchSessionException;
import tk.zhangh.service.user.domain.model.userevent.UserEvent;
import tk.zhangh.service.user.domain.model.userevent.UserEventEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static tk.zhangh.service.user.domain.model.userevent.UserEventType.LOGGED_OUT;
import static tk.zhangh.service.user.domain.model.userevent.UserEventType.SIGNIN_REMEMBER_ME;

/**
 * Created by ZhangHao on 17/8/20.
 */
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final UserEventEmitter userEventEmitter;

    public SessionServiceImpl(SessionRepository sessionRepository, UserEventEmitter userEventEmitter) {
        this.sessionRepository = sessionRepository;
        this.userEventEmitter = userEventEmitter;
    }

    @Override
    public Session createSession(Long sessionId, Long userId, String token) {
        return createSession(sessionId, userId, token, 0);
    }

    @Override
    public Session createSession(Long sessionId, Long userId, String token, int minutes) {
        Objects.requireNonNull(userId);
        Session session = new Session(sessionId, userId, token, minutes);
        sessionRepository.save(session);

        log.info("Created persistent session {} for user {}.", session.getId(), session.getUserId());

        return session;
    }

    @Override
    public Optional<Session> findSession(Long id) {
        Objects.requireNonNull(id);
        return sessionRepository.findById(id).map(session -> (session.isValid() ? session : null));
    }

    @Override
    public Session getSession(Long id) throws NoSuchSessionException {
        return findSession(id).orElseThrow(NoSuchSessionException::new);
    }

    @Override
    public void logoutUser(Long userId) {
        List<Long> deletedSessionIds = sessionRepository.findByUserId(userId)
                .stream()
                .peek(session -> session.setDeleted(true))
                .peek(sessionRepository::save)
                .map(Session::getId)
                .collect(toList());

        log.info("Sessions {} of user {} were deleted.", deletedSessionIds, userId);

        userEventEmitter.emit(new UserEvent(userId, LOGGED_OUT));
    }

    @Override
    public void useSession(Long id, String value, LocalDateTime lastUsedAt) throws NoSuchSessionException {
        Session session = getSession(id);
        session.setToken(value);
        session.setLastUsedAt(lastUsedAt);

        sessionRepository.save(session);

        log.info(
                "Auto login with persistent session {} for user {}.", session.getId(), session.getUserId());

        userEventEmitter.emit(new UserEvent(session.getUserId(), SIGNIN_REMEMBER_ME));
    }
}
