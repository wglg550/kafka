package distributed.transaction.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Description: 经典的ABA问题与解决方法(jdk Atomic原子类操作)
 * AtomicStampReference
 * AtomicStampReference在cas的基础上增加了一个标记stamp，使用这个标记可以用来觉察数据是否发生变化
 * AtomicStampReference的使用实例
 * 我们定义了一个money值为19，然后使用了stamp这个标记，这样每次当cas执行成功的时候都会给原来的标记值+1。而后来的线程来执行的时候就因为stamp不符合条件而使cas无法成功，这就保证了每次只会被执行一次。
 * 比如 AtomicBoolean 可以用在这样一个场景下，系统需要根据一个布尔变量的状态属性来判断是否需要执行一些初始化操作，如果是多线程的环境下，避免多次重复执行，可以使用 AtomicBoolean 来实现，伪代码如下：
 * private final static AtomicBoolean flag = new AtomicBoolean();
 * if(flag.compareAndSet(false,true)){
 * init();
 * }
 * 比如 AtomicInteger 可以用在计数器中，多线程环境中，保证计数准确。
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/17
 */
public class AtomicStampReference {
    static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19, 0);

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            int stamp = money.getStamp();
            System.out.println("stamp的值是" + stamp);
            new Thread() {         //充值线程

                @Override
                public void run() {
                    while (true) {
                        Integer account = money.getReference();
                        if (account < 20) {
                            if (money.compareAndSet(account, account + 20, stamp, stamp + 1)) {
                                System.out.println("余额小于20元，充值成功，目前余额：" + money.getReference() + "元");
                                break;
                            }
                        } else {
                            System.out.println("余额大于20元,无需充值");
                        }
                    }
                }
            }.start();
        }

        new Thread() {

            @Override
            public void run() {    //消费线程
                for (int j = 0; j < 100; j++) {
                    while (true) {
                        int timeStamp = money.getStamp();//1
                        int currentMoney = money.getReference();//39
                        if (currentMoney > 10) {
                            System.out.println("当前账户余额大于10元");
                            if (money.compareAndSet(currentMoney, currentMoney - 10, timeStamp, timeStamp + 1)) {
                                System.out.println("消费者成功消费10元，余额" + money.getReference());
                                break;
                            }
                        } else {
                            System.out.println("没有足够的金额");
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            break;
                        }
                    }
                }
            }
        }.start();
    }
}
