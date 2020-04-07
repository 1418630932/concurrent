package demo.testDemo;

/**
 * @author zhuliyang
 * @date 2020-04-03
 * @time 0:42
 **/
public class SiSuo {
    Object o1 = new Object();
    Object o2 = new Object();
    public void test1(){
        synchronized (o1){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (o2){
                System.out.println(1123);
            }
        }
    }
    public void test2(){
        synchronized (o2){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (o1){
                System.out.println(1123);
            }
        }
    }

    public static void main(String[] args) {
        SiSuo siSuo = new SiSuo();
        new Thread(()->siSuo.test1()).start();
        new Thread(()->siSuo.test2()).start();
    }
}
