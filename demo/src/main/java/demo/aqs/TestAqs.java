package demo.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author zhuliyang
 * @date 2020-04-16
 * @time 23:01
 **/
public class TestAqs implements Lock {
    //同步器类
    class MySync extends AbstractQueuedSynchronizer{
        //尝试加锁方法
        @Override
        protected boolean tryAcquire(int arg) {
            //状态设置为1
            if (compareAndSetState(0,1 )){
                //设置owner 为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        //尝试解锁方法
        @Override
        protected boolean tryRelease(int arg) {
            setState(0);//状态设置为0
            setExclusiveOwnerThread(null);//放弃锁的持有权
            return true;
        }
        //是否占有锁
        @Override
        protected boolean isHeldExclusively() {
            return getState()==1;
        }
        public Condition newCondition(){
            return new ConditionObject();
        }
    }
    private MySync mySync = new MySync();

    //加锁
    @Override
    public void lock() {
        mySync.acquire(1);
    }
    //加锁 可打断
    @Override
    public void lockInterruptibly() throws InterruptedException {
        mySync.acquireInterruptibly(1);
    }
    //尝试加锁一次
    @Override
    public boolean tryLock() {
        return mySync.tryAcquire(1);
    }
    //尝试加锁一次 带超时
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mySync.tryAcquireNanos(1, unit.toNanos(time));
    }

    //解锁
    @Override
    public void unlock() {
        mySync.release(1);
    }
    //返回等待区
    @Override
    public Condition newCondition() {
        return mySync.newCondition();
    }
}
