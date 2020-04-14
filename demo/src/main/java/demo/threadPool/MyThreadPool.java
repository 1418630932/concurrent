package demo.threadPool;

import demo.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhuliyang
 * @date 2020-04-13
 * @time 0:07
 **/
@Slf4j
public class MyThreadPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(new BlockingQueue<>(10),
                2,
                1, TimeUnit.SECONDS,
                //拒绝策略
                //1 死等
//                (task,deque)->{deque.put(task);});
                //2.有时间的等待
//                (task,deque)->{deque.putByTimeOut(task,1, TimeUnit.SECONDS);});
                //3抛异常
//                (task,deque)->{throw new RuntimeException("任务执行失败");});
                //4 让调用者放弃多余的任务 不进入队列
                (task,deque)->{ log.debug("不进入队列"); });
                //5 让调用者执行多余的任务
//                (task,deque)->{ task.run(); });
        for (int i = 0; i <15 ; i++) {
            int j =i;
            threadPool.excute(() ->{
                Sleeper.sleep(3);
                System.out.println(j);
            });
        }
    }
}
@FunctionalInterface
interface Reject<T>{
    void doReject(T task,BlockingQueue<T> deque);
}

@Slf4j
class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;
    //线程集合
    private final HashSet<Worker> workers = new HashSet<>();
    //核心线程数
    private int coreSize;
    //获取任务的超时时间
    private long timeout;

    private TimeUnit timeUnit;

    private Reject<Runnable> reject;

    public void excute(Runnable task){
        synchronized (workers){
            if (workers.size()<coreSize){ //如果线程数 小于核心线程数
                Worker worker = new Worker(task);
                workers.add(worker);
                worker.start(); //异步启动线程
            }else {
                //执行拒绝策略
                taskQueue.putWithReject(reject,task);
            }
        }
    }

    //内部类 ：任务
    class Worker extends Thread{
        Runnable task ;
        public Worker(Runnable task){
            this.task = task;
        }
        @Override
        public void run() {
            workers.add(this);
            while (task!=null){
                try {
                    log.debug("正在执行{}",task);
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    //有限时间的阻塞获取  超时了就返回null 将会跳出while循环
                    task = taskQueue.takeByTimeOut(timeout, timeUnit);
                }
            }
            //如果线程获取任务超时  删除对应的线程
            synchronized (workers){
                log.debug("移除任务{}",this);
                workers.remove(this);
            }
        }
    }

    public ThreadPool(BlockingQueue<Runnable> taskQueue, int coreSize, long timeout, TimeUnit timeUnit,Reject<Runnable> reject) {
        this.taskQueue = taskQueue;
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.reject = reject;
    }


}
@Slf4j
class BlockingQueue<T>{
    //任务队列
    private Deque<T> deque = new ArrayDeque<>();
    //锁
    private ReentrantLock lock = new ReentrantLock();
    //生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //容量
    private int capcity;

    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    //阻塞获取队列头元素的方法
    public T take(){
        try {
            lock.lock();
            //如果队列为空 就阻塞
            while (!deque.isEmpty()){
                emptyWaitSet.await();//消费者线程等待
            }
            T poll = deque.poll();
            fullWaitSet.signal();//叫醒阻塞的生产者线程
            return poll ;//返回队列头部的元素
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    //阻塞获取队列头元素的方法
    public T takeByTimeOut(long timeOut, TimeUnit timeUnit){
        try {
            long nanos = timeUnit.toNanos(timeOut);//将超时时间转换成纳秒
            lock.lock();
            //如果队列为空 就阻塞
            while (deque.isEmpty()){
                if (nanos<=0){
                    log.debug(" 等待超时 ");
                    return null;
                }
                //awaitNanos() 返回  剩余的时间 =参数nanos - 被虚假唤醒时经过的时间
                nanos = emptyWaitSet.awaitNanos(nanos);//消费者线程等待纳秒级别
            }
            T poll = deque.poll();
            log.debug("获取队列任务",poll);
            fullWaitSet.signal();//叫醒阻塞的生产者线程
            return poll ;//返回队列头部的元素
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
    //阻塞给队列尾部添加元素
    public void put(T task){
        try {
            lock.lock();
            //如果队列满了 生产者线程等待
            while (deque.size()>=capcity){
                fullWaitSet.await();
            }
            log.debug("核心线程已满 往队列添加任务{}",task);
            deque.add(task);//往队列尾部添加元素
            emptyWaitSet.signal();//叫醒阻塞的消费者
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    //阻塞给队列尾部添加元素(带超时的)
    public boolean putByTimeOut(T task,long timeOut, TimeUnit timeUnit){
        try {
            long nanos = timeUnit.toNanos(timeOut);//将超时时间转换成纳秒
            lock.lock();
            //如果队列满了 生产者线程等待(有限时间的)
            while (deque.size()>=capcity){
                if (nanos<=0){
                    log.debug("添加超时");
                    return false;
                }
                nanos = fullWaitSet.awaitNanos(nanos);
            }
            log.debug("核心线程已满 往队列添加任务{}",task);
            deque.add(task);//往队列尾部添加元素
            emptyWaitSet.signal();//叫醒阻塞的消费者
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return true;
    }
    //获取队列大小
    public int size(){
        return deque.size();
    }

    //执行拒绝策略的 加入任务队列
    public void putWithReject(Reject<T> reject, T task) {
        try {
            lock.lock();
            if (deque.size()<capcity){
                log.debug("加入任务{}",task);
                deque.add(task);
            }else {
                reject.doReject( task, this);
                emptyWaitSet.signalAll();
            }
        }finally {
            lock.unlock();
        }
    }
}
