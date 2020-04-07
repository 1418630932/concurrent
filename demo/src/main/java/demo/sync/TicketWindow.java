package demo.sync;

import java.util.concurrent.atomic.AtomicInteger;

/**卖票
 * @author zhuliyang
 * @date 2020-04-01
 * @time 0:08
 **/
public class TicketWindow {
    int count = 10000;
    public int sell(){
        if (count>0){
            count--;
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) throws InterruptedException {
        TicketWindow ticketWindow = new TicketWindow();
        AtomicInteger sell = new AtomicInteger();
        for (int i = 0; i <5000 ; i++) {
            new Thread(()->{
                sell.addAndGet(ticketWindow.sell());
            }).start();
        }
        Thread.sleep(2000);
        System.out.println("卖出"+sell);
        System.out.println("剩余"+ticketWindow.count);
    }
}
