package cn.znnine.jvm.oopAllocate;

/**
 * 测试长期存活的对象会进入老年代
 *
 * 对象每在Survivor区中每熬过一次Minor GC年龄就会增加1岁，当它的年龄增加到一定程度（默认为15），就会被晋升到老年代中。
 *  晋升老年代的阈值可以通过-XX:MaxTenuringThreshold=15配置
 *
 * 但是年龄不是唯一晋升到老年代的条件，
 *      如果Survivor空间中相同年龄所有对象大小的总和大于Survivor的一半，年龄大于或等于该年龄的对象就可以直接进入老年代
 *
 *
 * @author lihongjian
 * @since 2021/8/10
 */
public class Test3 {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {

        //-verbose:gc -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:MaxTenuringThreshold=1
        /**
         * [GC (Allocation Failure) [DefNew: 6007K->592K(9216K), 0.0012250 secs] 6007K->592K(19456K), 0.0012712 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * [GC (Allocation Failure) [DefNew: 4936K->0K(9216K), 0.0008794 secs] 4936K->587K(19456K), 0.0008992 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * Heap
         *  def new generation   total 9216K, used 4233K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         *   eden space 8192K,  51% used [0x00000000fec00000, 0x00000000ff022450, 0x00000000ff400000)
         *   from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff4000f8, 0x00000000ff500000)
         *   to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
         *  tenured generation   total 10240K, used 587K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         *    the space 10240K,   5% used [0x00000000ff600000, 0x00000000ff692c30, 0x00000000ff692e00, 0x0000000100000000)
         *  Metaspace       used 3134K, capacity 4496K, committed 4864K, reserved 1056768K
         *   class space    used 343K, capacity 388K, committed 512K, reserved 1048576K
         */
        //-verbose:gc -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:MaxTenuringThreshold=15
        /**
         *[GC (Allocation Failure) [DefNew: 6171K->595K(9216K), 0.0014662 secs] 6171K->595K(19456K), 0.0015066 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * [GC (Allocation Failure) [DefNew: 4775K->20K(9216K), 0.0009747 secs] 4775K->610K(19456K), 0.0009931 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
         * Heap
         *  def new generation   total 9216K, used 4253K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
         *   eden space 8192K,  51% used [0x00000000fec00000, 0x00000000ff022508, 0x00000000ff400000)
         *   from space 1024K,   2% used [0x00000000ff400000, 0x00000000ff4052b0, 0x00000000ff500000)
         *   to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
         *  tenured generation   total 10240K, used 590K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
         *    the space 10240K,   5% used [0x00000000ff600000, 0x00000000ff6938f8, 0x00000000ff693a00, 0x0000000100000000)
         *  Metaspace       used 3162K, capacity 4496K, committed 4864K, reserved 1056768K
         *   class space    used 343K, capacity 388K, committed 512K, reserved 1048576K
         */

        testTenuringThreshold();
    }

    public static void testTenuringThreshold() {
        byte[] allocation1, allocation2, allocation3,allocation4,allocation5,allocation6,allocation7;
        allocation1 = new byte[_1MB / 4]; // 什么时候进入老年代决定于XX:MaxTenuringThreshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation4 = new byte[4 * _1MB];
        allocation5 = new byte[4 * _1MB];
        allocation6 = new byte[4 * _1MB];
        allocation7 = new byte[4 * _1MB];
        allocation3 = null;
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        allocation4 = new byte[4 * _1MB];
    }

}
