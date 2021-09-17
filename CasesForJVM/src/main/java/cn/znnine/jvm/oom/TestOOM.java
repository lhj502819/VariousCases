package cn.znnine.jvm.oom;

/**
 * @author lihongjian
 * @since 2021/7/18
 */
public class TestOOM {
    public static void main(String[] args) {
        String s1= new StringBuilder("计算机").append("软件").toString();

        System.out.println(s1 == s1.intern());

        String s2 = new StringBuilder("ja").append("va").toString();
        String s3 = new StringBuilder("ja").append("va").toString();

        String s4 = "java";
        String s5 = "java";
        System.out.println(s2 == s2.intern());
    }
}
