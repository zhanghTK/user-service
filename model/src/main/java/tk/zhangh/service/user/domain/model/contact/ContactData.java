package tk.zhangh.service.user.domain.model.contact;

import lombok.Data;
import tk.zhangh.service.user.commons.domain.ValueObject;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Data
public class ContactData implements ValueObject<ContactData> {
    private String email;
    private String firstName;
    private String lastName;
    private Set<AddressData> addresses;
    private Gender gender;
    private LocalDate birthday;

    @Override
    public boolean sameValueAs(ContactData other) {
        return equals(other);
    }
}
