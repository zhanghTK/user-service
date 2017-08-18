package tk.zhangh.service.user.commons.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public class Validator {
    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)"
                    + "+[\\w](?:[\\w-]*[\\w])?");

    private Validator() {
    }

    public static boolean isEmail(String email) {
        if (Objects.isNull(email)) {
            return false;
        }
        Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(email);
        return matcher.matches();
    }
}
