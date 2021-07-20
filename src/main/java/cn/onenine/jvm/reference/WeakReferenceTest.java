package cn.onenine.jvm.reference;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Description：测试弱引用，弱引用也是用来描述非必须对象的，比软引用的强度还弱一些，被弱引用关联的对象只能生存到下一次垃圾收集为止。
 *                  当垃圾收集器开始工作，无论当前内存是否足够，都会回收掉只被弱引用关联的对象，
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
    }

}
