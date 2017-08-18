package tk.zhangh.service.user.commons.domain;

import java.io.Serializable;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public interface ValueObject<T> extends Serializable {
    boolean sameValueAs(T other);
}
