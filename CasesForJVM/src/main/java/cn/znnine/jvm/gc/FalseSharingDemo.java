package cn.znnine.jvm.gc;

import sun.misc.Contended;

/**
 * Description：伪共享示例
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/7/23
 */
public class FalseSharingDemo {


    public static void main(String[] args) throws Exception{
        testFalseSharding(new Pointer());
    }


    public static void testFalseSharding(Pointer pointer) throws Exception{
        long startTime = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.a++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                pointer.b++;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(System.currentTimeMillis() - startTime);
    }

}

class Pointer {
    /**
     * 防止伪共享，CPU的缓存系统是以缓存行为单位存储的，每行大小为64字节，long类型占8字节，因此a和b会在同一个缓存行
     * 在多线程并发修改一个缓存行中的多个共享变量的时候，同一时刻只能有一个线程去操作该缓存行，将会导致性能的下降，
     * 因此1、我们可以多加几个变量把缓存行沾满，让b去另一个缓存行
     *    2、JDK8以后提供了sun.misc.Contended注解，通过@Contended注解就可以解决伪共享的问题，使用@Conntended注解后会增加128字节的padding，
     *          并且需要-XX:-RestrictContended选项后才能生效
     *
     */
    @Contended
    public volatile long a;

    @Contended
    public volatile long b;

}
