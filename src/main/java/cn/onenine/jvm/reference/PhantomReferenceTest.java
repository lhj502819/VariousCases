package cn.onenine.jvm.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;

/**
 * 虚引用测试  虚引用比软、弱引用还要弱，
 *
 * @author lihongjian
 * @since 2021/8/14
 */
public class PhantomReferenceTest {
    public static boolean isRun = true;

    public static void main(String[] args) throws Throwable {
        //创建150M的缓存数据
        byte[] cacheData = new byte[1024 * 1024 * 100];
        String abc = new String("abc");
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();

        new Thread(() -> {
            while (isRun) {
                Object obj = referenceQueue.poll();
                if (obj != null) {
                    try {
                        Field rereferent = Reference.class
                                .getDeclaredField("referent");
                        rereferent.setAccessible(true);
                        Object result = rereferent.get(obj);
                        System.out.println("gc will collect："
                                + result.getClass() + "@"
                                + result.hashCode() + "\t"
                                + (String) result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
        PhantomReference<String> abcWeakRef = new PhantomReference<String>(abc, referenceQueue);
        abc = null;
        Thread.sleep(3000);
        System.gc();
        Thread.sleep(3000);
        isRun = false;

    }
}
