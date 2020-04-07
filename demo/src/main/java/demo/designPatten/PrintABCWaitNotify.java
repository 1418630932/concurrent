package demo.designPatten;

/**
 * @author zhuliyang
 * @date 2020-04-06
 * @time 22:47
 **/
public class PrintABCWaitNotify {
    int flag = 1;

    public void print(int curflag, int nextFalg, String str) {
        synchronized (this) {
            for (int i = 0; i < 5; i++) {
                while (flag != curflag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                this.notifyAll();
                flag = nextFalg;
            }
        }
    }

    public static void main(String[] args) {
        PrintABCWaitNotify p = new PrintABCWaitNotify();
        new Thread(() -> { p.print(1, 2, "A"); }).start();
        new Thread(() -> { p.print(2, 3, "B"); }).start();
        new Thread(() -> { p.print(3, 1, "C"); }).start();
    }
}
