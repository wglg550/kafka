package distributed.transaction.threadFactory;

import java.util.concurrent.*;

/**
 * @Description: 线程池的使用
 * 在java doc中，并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池：
 * Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE,newCachedThreadPool将corePoolSize设置为0，将maximumPoolSize设置为Integer.MAX_VALUE，使用的SynchronousQueue，也就是说来了任务就创建线程运行，当线程空闲超过60秒，就销毁线程。
 * Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池,newSingleThreadExecutor将corePoolSize和maximumPoolSize都设置为1，也使用的LinkedBlockingQueue
 * Executors.newFixedThreadPool(int);    //创建固定容量大小的缓冲池,newFixedThreadPool创建的线程池corePoolSize和maximumPoolSize值是相等的，它使用的LinkedBlockingQueue
 * Executors.newScheduledThreadPool(int);//创建一个固定大小的线程池，线程池内线程存活时间无限制，线程池可以支持定时及周期性任务执行，如果所有线程均处于繁忙状态，对于新任务会进入DelayedWorkQueue队列中，这是一种按照超时时间排序的队列结构
 * Executors.newWorkStealingPool(); //fork/join并发线程池
 * <p>
 * Java 常用的线程池有7种，他们分别是：
 * 1.newCachedThreadPool ：创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * 2.newFixedThreadPool：创建一个固定数目的、可重用的线程池。
 * 3.newScheduledThreadPool:创建一个定长线程池，支持定时及周期性任务执行。
 * 4.newSingleThreadExecutor：创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 * 5.newSingleThreadScheduledExcutor：创建一个单例线程池，定期或延时执行任务。
 * 6.newWorkStealingPool:创建持有足够线程的线程池来支持给定的并行级别，并通过使用多个队列，减少竞争，它需要穿一个并行级别的参数，如果不传，则被设定为默认的CPU数量。
 * 7.ForkJoinPool：支持大任务分解成小任务的线程池，这是Java8新增线程池，通常配合ForkJoinTask接口的子类RecursiveAction或RecursiveTask使用。
 * <p>
 * ThreadPoolExecutor提供了两个方法，用于线程池的关闭，分别是shutdown()和shutdownNow()，其中
 * shutdown()：不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务
 * shutdownNow()：立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务
 * <p>
 * 在前面我们多次提到了任务缓存队列，即workQueue，它用来存放等待执行的任务。
 * workQueue的类型为BlockingQueue<Runnable>，通常可以取下面三种类型：
 * 1.ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；
 * 2.LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；
 * 3.synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。
 * <p>
 * 线程池任务执行流程：
 * 1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。
 * 2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行
 * 3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务
 * 4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理
 * 5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程
 * 6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭
 * <p>
 * 备注：
 * 一般如果线程池任务队列采用LinkedBlockingQueue队列的话，那么不会拒绝任何任务（因为队列大小没有限制），这种情况下，ThreadPoolExecutor最多仅会按照最小线程数来创建线程，也就是说线程池大小被忽略了。
 * 如果线程池任务队列采用ArrayBlockingQueue队列的话，那么ThreadPoolExecutor将会采取一个非常负责的算法，比如假定线程池的最小线程数为4，最大为8所用的ArrayBlockingQueue最大为10。随着任务到达并被放到队列中，线程池中最多运行4个线程（即最小线程数）。即使队列完全填满，也就是说有10个处于等待状态的任务，ThreadPoolExecutor也只会利用4个线程。如果队列已满，而又有新任务进来，此时才会启动一个新线程，这里不会因为队列已满而拒接该任务，相反会启动一个新线程。新线程会运行队列中的第一个任务，为新来的任务腾出空间。
 * 这个算法背后的理念是：该池大部分时间仅使用核心线程（4个），即使有适量的任务在队列中等待运行。这时线程池就可以用作节流阀。如果挤压的请求变得非常多，这时该池就会尝试运行更多的线程来清理；这时第二个节流阀—最大线程数就起作用了。
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/17
 */
public class ThreadFactoryMain {

    public static void min(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

        for (int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    executor.getQueue().size() + "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
        }
        executor.shutdown();
        System.out.println(8 >> 2);
        System.out.println(2 << 3);
    }

    static class MyTask implements Runnable {
        private int taskNum;

        public MyTask(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() {
            System.out.println("正在执行task " + taskNum);
            try {
                Thread.currentThread().sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task " + taskNum + "执行完毕");
        }
    }
}
