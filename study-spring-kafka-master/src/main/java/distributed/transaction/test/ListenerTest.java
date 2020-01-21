package distributed.transaction.test;

import distributed.transaction.event.Source;
import distributed.transaction.listener.StateChangeListener;
import distributed.transaction.listener.StateChangeToOneListener;

/**
 * @Description: 监听测试类
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/17
 */
public class ListenerTest {
    public static void main(String[] args) {
        Source source = new Source();
        source.addStateChangeListener(new StateChangeListener());
        source.addStateChangeToOneListener(new StateChangeToOneListener());

        source.changeFlag();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        source.changeFlag();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        source.changeFlag();
    }
}
