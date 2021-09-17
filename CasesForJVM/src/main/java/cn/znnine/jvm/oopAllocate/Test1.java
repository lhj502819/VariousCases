package cn.znnine.jvm.oopAllocate;

/**
 * 测试对象优先在Eden分配
 *
 * JVM参数：-verbose:gc -XX:+UseSerialGC -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails
 *
 * @author lihongjian
 * @since 2021/8/10
 */
public class Test1 {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {

        /**
         * [GC (Allocation Failure) [DefNew: 7963K->597K(9216K), 0.0018874 secs] 7963K->597K(19456K), 0.0019715 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * Heap
         *  def new generation   total 9216K, used 4859K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         *   eden space 8192K,  52% used [0x00000000fec00000, 0x00000000ff029798, 0x00000000ff400000)
         *   from space 1024K,  58% used [0x00000000ff500000, 0x00000000ff595630, 0x00000000ff600000)
         *   to   space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
         *  tenured generation   total 10240K, used 0K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         *    the space 10240K,   0% used [0x00000000ff600000, 0x00000000ff600000, 0x00000000ff600200, 0x0000000100000000)
         *  Metaspace       used 3206K, capacity 4496K, committed 4864K, reserved 1056768K
         *   class space    used 348K, capacity 388K, committed 512K, reserved 1048576K
         */
        testAllocation();

    }

    private static void testAllocation(){

        byte[] allocation1,allocation2,allocation3,allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation4 = new byte[4 * _1MB];

    }
}
