package tk.zhangh.service.user.domain.model.session;

import java.util.List;
import java.util.Optional;

/**
 * Created by ZhangHao on 17/8/20.
 */
public interface SessionRepository {

    Optional<Session> findById(Long id);

    List<Session> findByUserId(Long userId);

    Session save(Session session);
}
