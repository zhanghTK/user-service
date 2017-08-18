package tk.zhangh.service.user.service;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import tk.zhangh.service.user.domain.model.userevent.UserEvent;
import tk.zhangh.service.user.domain.model.userevent.UserEventEmitter;
import tk.zhangh.service.user.domain.model.userevent.UserEventType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by ZhangHao on 2017/8/18.
 */
public class BaseServiceTest {

    @Mock  // @Mock 使用注解方式创建 mock 对象
            UserEventEmitter userEventEmitter;

    /**
     * 断言事件类型
     */
    protected void assertEmittedUserEvent(UserEventType expectedUserEventType) {
        UserEvent userEvent = captureEmittedUserEvent();
        assertEquals(expectedUserEventType, userEvent.getUserEventType());
    }

    protected UserEvent captureEmittedUserEvent() {
        ArgumentCaptor<UserEvent> userEventCaptor = ArgumentCaptor.forClass(UserEvent.class);  // 创建参数捕获器
        verify(userEventEmitter).emit(userEventCaptor.capture());  // 捕获参数
        return userEventCaptor.getValue();  // 获取方法参数值
    }
}
