package tk.zhangh.service.user.domain.model.contact;

import lombok.Getter;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Getter
public enum Country {
    CN("China", "CHN", 156);

    private final String shortName;
    private final String alpha3Code;
    private final int numericCode;

    Country(String shortName, String alpha3Code, int numericCode) {
        this.shortName = shortName;
        this.alpha3Code = alpha3Code;
        this.numericCode = numericCode;
    }
}
