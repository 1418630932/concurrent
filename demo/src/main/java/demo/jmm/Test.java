package demo.jmm;

import demo.util.Sleeper;

/**
 * @author zhuliyang
 * @date 2020-04-07
 * @time 23:11
 **/
public class Test {
    static volatile   boolean  run = true;
    public static void main(String[] args) {
        new Thread(() ->{
            while (run){

            }
            System.out.println("jeishule1");
        }).start();
        Sleeper.sleep(5);
        run=false;
    }
}
