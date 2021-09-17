package cn.znnine.jvm.classloader;

/**
 * 测试启动类加载器是获取不到的
 *
 * @author lihongjian
 * @since 2021/8/28
 */
public class BootStrapClassLoaderTest {
    public static void main(String[] args) {
        System.out.printf("java.lang.String的类加载器是： %s%n", String.class.getClassLoader());
    }
}
