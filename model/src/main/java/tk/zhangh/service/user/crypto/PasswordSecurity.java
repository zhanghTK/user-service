package tk.zhangh.service.user.crypto;

import tk.zhangh.service.user.domain.model.user.Password;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public interface PasswordSecurity {

    boolean check(Password pwd, String rawPwd);

    Password ecrypt(String rawPwd);
}
