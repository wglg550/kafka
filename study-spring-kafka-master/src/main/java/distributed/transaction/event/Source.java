package distributed.transaction.event;

import distributed.transaction.listener.StateChangeListener;
import distributed.transaction.listener.StateChangeToOneListener;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description: 典型的观察者模式，EventListener的增删改和通知
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/17
 */
public class Source {

    private int flag = 0;
    Set<EventListener> listeners = new HashSet<EventListener>();

    /**
     * 注册事件监听器
     *
     * @param listener
     */
    public void addStateChangeListener(StateChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * 注册事件监听器
     *
     * @param listener
     */
    public void addStateChangeToOneListener(StateChangeToOneListener listener) {
        listeners.add(listener);
    }

    /**
     * 当事件发生时，通知注册在事件源上的所有事件做出相应的反映
     */
    public void notifyListener() {
        for (EventListener listener : listeners) {
            try {
                ((StateChangeListener) listener).handleEvent(new MyEvent(this));
            } catch (Exception e) {
                if (flag == 1) {
                    ((StateChangeToOneListener) listener).handleEvent(new MyEvent(this));
                }
            }
        }
    }

    /**
     * 改变状态
     */
    public void changeFlag() {
        flag = (flag == 0 ? 1 : 0);
        notifyListener();
    }

    public int getFlag() {
        return flag;
    }
}
