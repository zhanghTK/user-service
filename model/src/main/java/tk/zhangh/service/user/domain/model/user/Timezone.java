package tk.zhangh.service.user.domain.model.user;

import java.time.ZoneId;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public enum Timezone {
    ASIA_SHANGHAI("Asia/Shanghai");

    private final String zoneName;
    private final ZoneId zoneId;

    Timezone(String zoneName) {
        this.zoneName = zoneName;
        this.zoneId = ZoneId.of(zoneName);
    }
}
