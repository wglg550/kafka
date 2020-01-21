package distributed.transaction.runnable;

public class MyThread implements Runnable {
    private static volatile int ticket = 5;

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(500);// 睡眠0.5秒  不至于运行太快
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.ticket > 0) {
                System.out.println("线程开始：" + Thread.currentThread().getName() + ",卖票：" + this.ticket--);
            }
        }
    }

    public static void main(String[] args) {
//        MyThread mt1 = new MyThread();
//        MyThread mt2 = new MyThread();
//        MyThread mt3 = new MyThread();
//        mt1.start();//每个线程都各卖了5张，共卖了15张票
//        mt2.start();//但实际只有5张票，每个线程都卖自己的票
//        mt3.start();//没有达到资源共享
        MyThread mt = new MyThread();
        new Thread(mt).start();//同一个mt，但是在Thread中就不可以，如果用同一
        new Thread(mt).start();//个实例化对象mt，就会出现异常
        new Thread(mt).start();
    }
}
