package distributed.transaction.listener;

import distributed.transaction.event.MyEvent;

import java.util.EventListener;

/**
 * @Description: 具体监听事件1
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/17
 */
public class StateChangeListener implements EventListener {

    public void handleEvent(MyEvent event) {
        System.out.println("触发状态改变事件。。。");
        System.out.println("当前事件源状态为：" + event.getSourceState());
        System.out.println("。。。。。。。。。。。。。。。。。。。。。。。");
    }
}