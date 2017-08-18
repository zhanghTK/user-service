package tk.zhangh.service.user.commons.domain;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public interface DomainEvent<T> {
    boolean sameEventAs(T other);
}
