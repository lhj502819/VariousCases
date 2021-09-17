package cn.znnine.jvm.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description：测试类加载器
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/8/27
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ClassNotFoundException();
                }
            }

        };
        Object obj = classLoader.loadClass("cn.znnine.jvm.classloader.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof ClassLoaderTest);
    }
}
