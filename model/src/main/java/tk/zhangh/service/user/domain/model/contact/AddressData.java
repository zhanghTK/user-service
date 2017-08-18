package tk.zhangh.service.user.domain.model.contact;

import lombok.Data;
import tk.zhangh.service.user.commons.domain.ValueObject;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Data
public class AddressData implements ValueObject<AddressData> {

    private Country country;
    private State state;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String zipCode;
    private AddressType addressType;

    @Override
    public boolean sameValueAs(AddressData other) {
        return equals(other);
    }
}
