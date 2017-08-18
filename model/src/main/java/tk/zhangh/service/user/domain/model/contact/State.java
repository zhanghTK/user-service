package tk.zhangh.service.user.domain.model.contact;

import lombok.Getter;

/**
 * Created by ZhangHao on 2017/8/18.
 */
@Getter
public enum State {

    GD("GuangDong");

    private String state;

    State(String state) {
        this.state = state;
    }
}
