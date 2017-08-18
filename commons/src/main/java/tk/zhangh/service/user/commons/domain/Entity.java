package tk.zhangh.service.user.commons.domain;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public interface Entity<I, E> {

    boolean sameIdentityAs(E other);

    I getId();

    default void setId(I id) {
    }

    default boolean isNew() {
        return getId() == null;
    }
}
