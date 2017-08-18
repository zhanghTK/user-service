package tk.zhangh.service.user.domain.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.zhangh.service.user.commons.domain.AuditData;
import tk.zhangh.service.user.commons.domain.Entity;
import tk.zhangh.service.user.commons.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;
import static tk.zhangh.service.user.commons.util.DateTimeUtil.expireNowUtc;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "value")
@NoArgsConstructor
public class ConfirmationToken<P> implements Entity<Long, ConfirmationToken<P>> {

    public static final int DEFAULT_EXPIRATION_MINUTES = 10;

    private Long id;
    private String value;
    private User owner;
    private ConfirmationTokenType type;
    private boolean valid = true;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private P payload;
    private AuditData<User> auditData;

    public ConfirmationToken(User owner, ConfirmationTokenType type) {
        this(owner, type, DEFAULT_EXPIRATION_MINUTES);
    }

    public ConfirmationToken(User owner, ConfirmationTokenType type, int minutes) {
        // TODO 使用更复杂的生成规则
        this(owner, UUID.randomUUID().toString(), type, minutes);
    }

    public ConfirmationToken(User owner, String value, ConfirmationTokenType type, int minutes) {
        this(owner, value, type, minutes, null);
    }

    public ConfirmationToken(User owner, String value, ConfirmationTokenType type) {
        this(owner, value, type, DEFAULT_EXPIRATION_MINUTES, null);
    }

    public ConfirmationToken(User owner, String value, ConfirmationTokenType type, int minutes, P payload) {
        this.owner = owner;
        this.value = value;
        this.type = type;
        this.expiresAt = expireNowUtc(minutes, MINUTES);
        this.payload = payload;
    }

    public ConfirmationToken use() {
        valid = false;
        usedAt = DateTimeUtil.nowUtc();
        return this;
    }

    @Override
    public boolean sameIdentityAs(ConfirmationToken<P> other) {
        return equals(other);
    }

    public Optional<P> getPayload() {
        return Optional.ofNullable(payload);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
