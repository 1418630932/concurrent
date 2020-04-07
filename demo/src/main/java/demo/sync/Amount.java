package demo.sync;

/**
 * @author zhuliyang
 * @date 2020-04-01
 * @time 0:51
 **/
public class Amount {
    int amount = 1000;

    public  void transfer(Amount anOther, int money) {//给对方账户转账
        this.amount -= money;
        anOther.amount += money;
    }

    public static void main(String[] args) throws InterruptedException {
        Amount a = new Amount();
        Amount b = new Amount();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, 1);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, 1);
            }
        });
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println(a.amount+b.amount);
    }
}
