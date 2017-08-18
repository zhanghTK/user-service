package tk.zhangh.service.user.commons.util;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

import static java.time.Clock.systemUTC;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public final class DateTimeUtil {
    private DateTimeUtil() {}

    public static LocalDateTime expireNowUtc(int time, TemporalUnit unit) {
        return nowUtc().plus(time, unit);
    }

    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(systemUTC());
    }
}
