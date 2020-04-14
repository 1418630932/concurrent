package demo.cas;

import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
import demo.util.Sleeper;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author zhuliyang
 * @date 2020-04-12
 * @time 0:17
 **/
public class MyAtomicInteger {
    Unsafe unsafe;
    int value;
    long valueOffset;
    public MyAtomicInteger(int initValue) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        //反射获取对象
        this.unsafe = (Unsafe) theUnsafe.get(null);
        Field fieldValue = MyAtomicInteger.class.getDeclaredField("value");
        //获取域的偏移地址
        this.valueOffset = unsafe.objectFieldOffset(fieldValue);
        this.value = initValue;
    }
    public void add(int add){
            int prev = value;
            int post = value+add;
            if(!unsafe.compareAndSwapInt(this, valueOffset,prev , post)){
                System.out.println(false);
            }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        MyAtomicInteger myAtomicInteger = new MyAtomicInteger(0);
        for (int i = 0; i <1000 ; i++) {
            new Thread(()->{myAtomicInteger.add(1);}).start();
        }
        Sleeper.sleep(3);//等3秒
        System.out.println(myAtomicInteger.value);
    }
}
