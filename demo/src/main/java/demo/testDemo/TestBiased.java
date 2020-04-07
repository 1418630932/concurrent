package demo.testDemo;

/** 偏向级锁升级轻量级锁
 * @author zhuliyang
 * @date 2020-04-04
 * @time 17:02
 **/
public class TestBiased {
    private static void test2() throws InterruptedException {
        Dog d = new Dog();
        Thread t1 = new Thread(() -> {
            synchronized (d) {
                System.out.println();
            }
            synchronized (TestBiased.class) {
                TestBiased.class.notify();
            }
            // 如果不用 wait/notify 使用 join 必须打开下面的注释
            // 因为：t1 线程不能结束，否则底层线程可能被 jvm 重用作为 t2 线程，底层线程 id 是一样的
 /*try {
 System.in.read();
 } catch (IOException e) {
 e.printStackTrace();
 }*/
        }, "t1");
        t1.start();
        Thread t2 = new Thread(() -> {
            synchronized (TestBiased.class) {
                try {
                    TestBiased.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
            synchronized (d) {
                System.out.println();
            }
            System.out.println();
        }, "t2");
        t2.start();
    }
}
class Dog {

}
