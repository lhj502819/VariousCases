package cn.znnie.demo;

import com.google.common.util.concurrent.RateLimiter;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description：{@link com.google.common.util.concurrent.RateLimiter}
 *
 * @author li.hongjian
 * @email lihongjian01@51talk.com
 * @Date 2024/1/3
 */
public class GuavaRateLimiterTest {

    public static void main(String[] args) throws Exception{
        RateLimiter rateLimiter = RateLimiter.create(100);
        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);
        long startTime = Instant.now().toEpochMilli();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                double waitTime = rateLimiter.acquire(1);
                System.out.printf("waite time: %s，current time：%d\n",waitTime,System.currentTimeMillis());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.printf("cost: %d", Instant.now().toEpochMilli() - startTime);
    }

}
