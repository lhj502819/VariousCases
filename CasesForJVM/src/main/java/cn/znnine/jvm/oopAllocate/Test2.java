package cn.znnine.jvm.oopAllocate;

/**
 * 测试大对象直接在Old区分配
 *
 * JVM参数：-XX:+UseSerialGC -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 *  通过-XX:PretenureSizeThreshol指定大于该值的对象直接在老年代分配，避免大对象在Eden区和Survivor之间来回复制，产生大量的内存复制操作，这里只能设置kb数
 *
 * @author lihongjian
 * @since 2021/8/10
 */
public class Test2 {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        /**
         * Heap
         *  def new generation   total 9216K, used 1983K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         *   eden space 8192K,  24% used [0x00000000fec00000, 0x00000000fedeffc0, 0x00000000ff400000)
         *   from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
         *   to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
         *  tenured generation   total 10240K, used 4096K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         *          可以看到Old区被使用了4MB，就是allocation对象
         *    the space 10240K,  40% used [0x00000000ff600000, 0x00000000ffa00010, 0x00000000ffa00200, 0x0000000100000000)
         *  Metaspace       used 3133K, capacity 4496K, committed 4864K, reserved 1056768K
         *   class space    used 343K, capacity 388K, committed 512K, reserved 1048576K
         */
        testPretenyreSuzeThreshold();
    }
    public static void testPretenyreSuzeThreshold(){
        byte[] allocation = new byte[ 4 * _1MB];
    }


}
