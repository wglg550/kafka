package distributed.transaction.runnable;

public class TestThread {

    public static void main(String[] args) {
        MyThread mt = new MyThread();
        new Thread(mt).start();//同一个mt，但是在Thread中就不可以，如果用同一
        new Thread(mt).start();//个实例化对象mt，就会出现异常
        new Thread(mt).start();
    }
}
