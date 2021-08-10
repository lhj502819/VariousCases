package cn.onenine.jvm.oopAllocate;

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
        testPretenyreSuzeThreshold();
    }
    public static void testPretenyreSuzeThreshold(){
        byte[] allocation = new byte[ 4 * _1MB];
    }


}
