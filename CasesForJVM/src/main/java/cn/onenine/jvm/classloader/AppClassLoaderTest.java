package cn.onenine.jvm.classloader;

/**
 * 测试获取应用类加载器
 *
 * @author lihongjian
 * @since 2021/8/28
 */
public class AppClassLoaderTest {
    public static void main(String[] args) {
        System.out.println(ClassLoader.getSystemClassLoader());;
        System.out.println(ClassLoader.getSystemClassLoader().getClass());;
    }
}
