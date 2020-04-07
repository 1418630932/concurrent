package demo.sync;

/**线程8锁
 * @author zhuliyang
 * @date 2020-03-31
 * @time 1:11
 **/
public class ThreadEight {
    public static synchronized void a() {
//        Thread.sleep(1000);
        System.out.println(1);
    }
    public static synchronized void b() {
        System.out.println(2);
    }

    public static void main(String[] args) {
        ThreadEight threadEight = new ThreadEight();
        ThreadEight threadEight2 = new ThreadEight();
        new Thread(() -> threadEight.a()).start();
        new Thread(() -> threadEight2.b()).start();
    }
}
