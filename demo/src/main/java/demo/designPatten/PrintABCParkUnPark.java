package demo.designPatten;

import demo.util.Sleeper;

import java.util.concurrent.locks.LockSupport;

/**
 * @author zhuliyang
 * @date 2020-04-06
 * @time 23:44
 **/
public class PrintABCParkUnPark {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
         t1= new Thread(() ->{
            for (int i = 0; i <5 ; i++) {
                LockSupport.park();
                System.out.print("A");
                LockSupport.unpark(t2);
            }
        });
        t2= new Thread(() ->{
            for (int i = 0; i <5 ; i++) {
                LockSupport.park();
                System.out.print("B");
                LockSupport.unpark(t3);
            }
        });
        t3= new Thread(() ->{
            for (int i = 0; i <5 ; i++) {
                LockSupport.park();
                System.out.print("C");
                LockSupport.unpark(t1);
            }
        });
        t1.start();
        t2.start();
        t3.start();
        LockSupport.unpark(t1);
    }
}
