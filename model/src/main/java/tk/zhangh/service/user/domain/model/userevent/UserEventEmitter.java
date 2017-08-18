package tk.zhangh.service.user.domain.model.userevent;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@FunctionalInterface
public interface UserEventEmitter {
    void emit(UserEvent userEvent);
}
