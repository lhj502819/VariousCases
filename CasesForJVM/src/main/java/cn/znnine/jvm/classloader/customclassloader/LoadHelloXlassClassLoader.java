package cn.znnine.jvm.classloader.customclassloader;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。
 *
 * @author lihongjian
 * @since 2021/8/28
 */
public class LoadHelloXlassClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
