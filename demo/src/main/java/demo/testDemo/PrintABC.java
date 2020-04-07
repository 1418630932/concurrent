package demo.testDemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhuliyang
 * @date 2020-04-05
 * @time 21:54
 **/
public class PrintABC {
    static int a = 0;

    public static void main(String[] args) {
        Object lock = new Object();

        Thread t1 = new Thread(() -> {
            while (a <= 30) {
                synchronized (lock) {
                    if (a % 3 == 0) {
                        System.out.println("a");
                        a++;
                    }
                }
            }
        });
        Thread t2 = new Thread(() -> {
            while (a <= 30) {
                synchronized (lock) {
                    if (a % 3 == 1) {
                        System.out.println("b");
                        a++;
                    }
                }
            }
        });
        Thread t3 = new Thread(() -> {
            while (a <= 30) {
                synchronized (lock) {
                    if (a % 3 == 2) {
                        System.out.println("c");
                        a++;
                    }
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }
}
