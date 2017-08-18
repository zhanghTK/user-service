package tk.zhangh.service.user.commons.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Data
public class AuditData<U> implements ValueObject<AuditData<U>> {

    private LocalDateTime createdAt;
    private U createdBy;
    private LocalDateTime modifiedAt;
    private U modifiedBy;

    public boolean sameValueAs(AuditData<U> other) {
        return false;
    }
}
