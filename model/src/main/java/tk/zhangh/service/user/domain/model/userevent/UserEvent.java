package tk.zhangh.service.user.domain.model.userevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import tk.zhangh.service.user.commons.domain.DomainEvent;

import java.util.Optional;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Data
@AllArgsConstructor
public class UserEvent<P> implements DomainEvent<UserEvent> {

    private final Long userId;
    private final UserEventType userEventType;
    private final P payload;

    public UserEvent(Long userId, UserEventType userEventType) {
        this(userId, userEventType, null);
    }

    public Optional<P> getPayload() {
        return Optional.ofNullable(payload);
    }

    @Override
    public boolean sameEventAs(UserEvent other) {
        return equals(other);
    }
}
