package tk.zhangh.service.user.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.zhangh.service.user.commons.domain.ValueObject;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Password implements ValueObject<Password> {

    private String pwdHash;
    private String pwdSalt;


    @Override
    public boolean sameValueAs(Password other) {
        return equals(other);
    }
}
