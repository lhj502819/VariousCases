package cn.onenine.jvm.reference;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 弱引用也是用来描述非必须对象的，比软引用的强度还弱一些，被弱引用关联的对象只能生存到下一次垃圾收集为止。
 * 当垃圾收集器开始工作，无论当前内存是否足够，都会回收掉只被弱引用关联的对象，
 *
 * 1、测试弱引用的生命周期
 * 2、弱引用的作用
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/7/20
 */
public class WeakReferenceTest {

    /**
     * 未指定堆内存最大值
     */
    public static void main(String[] args) throws Exception{
        //创建150M的缓存数据
        byte[] cacheData = new byte[1024 * 1024 * 100];
        //将缓存数据用软引用持有
        WeakReference<byte[]> softReference = new WeakReference<>(cacheData);
        //将缓存数据的强引用去除
        cacheData = null;
        System.out.println("第一次GC前：" + softReference.get());
        System.gc();
        Thread.sleep(500);
        System.out.println("第一次GC后：" +  softReference.get());
        //再次创建一个120M的大对象
        byte[] newData = new byte[1024 * 1024 * 120];
        System.gc();
        Thread.sleep(500);
        System.out.println("再次创建对象后：" + softReference.get());

        /**
         * 执行结果为：
         *  [第一次GC前：[B@677327b6
         *   第一次GC后：null
         *   再次创建对象后：null]
         */


        System.out.println("---------------------------一条优雅的分割线---------------------------");
        /**
         * 从上边的情况看弱引用的作用和强引用其实是一样的，只要不被强引用关联则就会被回收，那么JDK官方设计它的目的是什么呢？请先看下边的案例
         */
        Map<String,Object> cacheMap = new HashMap<>(3);
        String key = "X";
        cacheMap.put(key , "x");
        /**
         * 假设在某种条件下我发现某个key对应的value对象没用了，打算把这个对象抛弃，通过key=null后，
         *    由于在Map底层有对这个对象的引用，因此也不能使GC回收掉对象
         * 因此设计了WeakHashMap类，具体WeakHashMap是如果工作的，请大家自行查阅
         */



    }

}
