package tk.zhangh.service.user.domain.model.userevent;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public enum UserEventType {
    DELETED,
    EMAIL_CHANGED,
    EMAIL_CHANGE_REQUESTED,
    EMAIL_CONFIRMED,
    LOGGED_OUT,
    PASSWORD_CHANGED,
    PASSWORD_RESET_CONFIRMED,
    PASSWORD_RESET_REQUESTED,
    SCREEN_NAME_CHANGED,
    SIGNIN_SUCCEEDED,
    SIGNIN_FAILED,
    SIGNIN_REMEMBER_ME,
    SIGNUP_REQUESTED
}
