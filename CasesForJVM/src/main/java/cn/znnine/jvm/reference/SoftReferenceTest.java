package cn.znnine.jvm.reference;

import java.lang.ref.SoftReference;

/**
 * Description：测试软引用，用来描述一些还有用，但非必须的东西。只是被软引用关联着的对象，
 *  在系统将要发生内存溢出的之前，会把这些对象列入回收范围中，进行第二次回收，如果这次回收还没有足够的内存，才会抛出内存溢出异常。
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/7/20
 */
public class SoftReferenceTest {

    /**
     * 执行参数 -Xmx200m
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable{
        //创建150M的缓存数据
        byte[] cacheData = new byte[1024 * 1024 * 100];
        //将缓存数据用软引用持有
        SoftReference<byte[]> softReference = new SoftReference<>(cacheData);
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
         * 执行结果为
         *   [第一次GC前：[B@677327b6
         *    第一次GC后：[B@677327b6
         *    再次创建对象后：null]
         *
         *   通过结果可以看到，当内存足够时，GC不会回收被软引用关联的对象，
         *      当堆内存不足以为新创建的对象分配内存时，将会回收掉被软引用关联的对象
         */
    }

}
