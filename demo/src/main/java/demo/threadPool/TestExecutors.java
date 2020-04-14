package demo.threadPool;

import demo.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author zhuliyang
 * @date 2020-04-15
 * @time 0:07
 **/
@Slf4j
public class TestExecutors {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        //执行一个任务列表  并且返回任务返回值列表
        List<Future<String>> futures = threadPool.invokeAll(Arrays.asList(
                () -> {
                    Sleeper.sleep(3);
                    log.debug("task1");
                    return "1";
                },
                () -> {
                    Sleeper.sleep(3);
                    log.debug("task2");
                    return "2";
                },
                () -> {
                    Sleeper.sleep(3);
                    log.debug("task3");
                    return "3";
                }
        ));
        futures.forEach(f -> {
            try {
                log.debug(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
