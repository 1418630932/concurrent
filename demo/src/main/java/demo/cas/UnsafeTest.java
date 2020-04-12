package demo.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zhuliyang
 * @date 2020-04-11
 * @time 22:43
 **/
public class UnsafeTest {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        LongAdder longAdder = new LongAdder();
        //反射获取unsafe 的thUnsafe属性
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        Man man = new Man();
        Field name = Man.class.getDeclaredField("name");
        Field age = Man.class.getDeclaredField("age");
        //获取域的偏移地址
        long offset1 = unsafe.objectFieldOffset(name);
        long offset2 = unsafe.objectFieldOffset(age);
        //cas设置 数据
        unsafe.compareAndSwapObject(man, offset1, null, "张三");
        unsafe.compareAndSwapInt(man, offset2, 0,22);
        System.out.println(man.name+":"+man.age);
    }
}

class Man {
    String name;
    int age;
}
