package cn.onenine.jvm.constantpool;

/**
 * Description：测试String常量池
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/8/3
 */
public class StringTest {
    private  static String s1 = "static";
    public static void main(String[] args) {
//        String hello1 = new String("hell") + new String("o");
//        String hello2 = new String("he") + new String("llo");
//        String hello3 = hello1.intern();
//        String hello4 = hello2.intern();
//        System.out.println(hello1 == hello3);
//        System.out.println(hello1 == hello4);
//        System.out.println(hello1 == hello2);
        String s1 = "hell";
        String s2 = "hell";
    }
}
