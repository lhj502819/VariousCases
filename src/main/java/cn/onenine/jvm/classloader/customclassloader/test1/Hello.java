package cn.onenine.jvm.classloader.customclassloader.test1;
/**
 * 自定义类加载器，将Hello.class加密
 *
 * @author lihongjian
 * @since 2021/8/28
 */
public class Hello {
    static {
        System.out.println("Hello Class Init.....");
    }
}
