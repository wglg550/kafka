package distributed.transaction.event;

import java.util.EventObject;

/**
 * @Description: 事件监听Object，所有监听都要继承EventObject
 * Spring @EventListener是注解方式实现的监听器
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/17
 */
public class MyEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private int sourceState;

    public MyEvent(Object source) {
        super(source);
        sourceState = ((Source) source).getFlag();
    }

    public int getSourceState() {
        return sourceState;
    }

}

