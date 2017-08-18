package tk.zhangh.service.user.domain.model.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.zhangh.service.user.commons.domain.Entity;
import tk.zhangh.service.user.commons.util.DateTimeUtil;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;
import static tk.zhangh.service.user.commons.util.DateTimeUtil.nowUtc;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Session implements Entity<Long, Session> {

    public static final int DEFAULT_EXPIRATION_MINUTES = 30 * 24 * 60;

    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime issuedAt;
    private LocalDateTime lastUsedAt;
    private LocalDateTime removedAt;
    private boolean deleted;

    public Session(Long id, Long userId, String token) {
        this(id, userId, token, DEFAULT_EXPIRATION_MINUTES);
    }

    public Session(Long id, Long userId, String token, int minutes) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        if (minutes == 0) {
            minutes = DEFAULT_EXPIRATION_MINUTES;
        }
        this.expiresAt = DateTimeUtil.expireNowUtc(minutes, MINUTES);
        this.issuedAt = nowUtc();
    }

    public Session(Long id, Long userId, String token, LocalDateTime expiresAt, LocalDateTime issuedAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.issuedAt = issuedAt;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
        this.removedAt = deleted ? nowUtc() : null;
    }

    public boolean isValid() {
        LocalDateTime now = nowUtc();
        return isValid(now);
    }

    public boolean isValid(LocalDateTime now) {
        return expiresAt.isAfter(now) && !deleted;
    }

    @Override
    public boolean sameIdentityAs(Session other) {
        return false;
    }


}
