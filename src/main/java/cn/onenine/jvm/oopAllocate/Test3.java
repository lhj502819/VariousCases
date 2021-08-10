package cn.onenine.jvm.oopAllocate;

/**
 * 测试长期存活的对象会进入老年代
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
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4]; // 什么时候进入老年代决定于XX:MaxTenuringThreshold设置
        allocation2 = new byte[4 * _1MB];
        allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }

}
