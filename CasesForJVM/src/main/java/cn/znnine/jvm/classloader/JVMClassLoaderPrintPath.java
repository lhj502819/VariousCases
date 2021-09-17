package cn.znnine.jvm.classloader;

import sun.misc.Launcher;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;

/**     -Dsun.boot.class.path="E:\software\JDK\lib\rt.jar"
 * java ‐Dsun.boot.class.path="E:\software\JDK\lib\rt.jar" 启动类
 * 打印类加载器加载了哪些类
 *
 * @author lihongjian
 * @since 2021/8/29
 */
public class JVMClassLoaderPrintPath {
    public static void main(String[] args) {
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();

        System.out.println("启动类加载器");

        Arrays.stream(urLs).map(URL::toExternalForm).forEach(System.out::println);

        System.out.println("扩展类加载器");

        printClassLoader("扩展类加载器", JVMClassLoaderPrintPath.class.getClassLoader().getParent());

        printClassLoader("应用类加载器" , JVMClassLoaderPrintPath.class.getClassLoader());
    }

    private static void printClassLoader(String name, ClassLoader classLoader) {
        if (classLoader != null) {
            System.out.println(name + " ClassLoader -> " + classLoader.toString());
            printURLForClassLoader(classLoader);
        }else {
            System.out.println(name + "ClassLoader -> null");
        }
    }

    private static void printURLForClassLoader(ClassLoader classLoader) {
        Object ucp = insightField(classLoader , "ucp");

        Object path = insightField(ucp , "path");
        ArrayList ps = (ArrayList)path;

        for (Object p : ps) {
            System.out.println("==>" + p.toString());
        }
    }

    private static Object insightField(Object obj, String name) {
        try {
            Field f = null;
            if(obj instanceof URLClassLoader){
                f = URLClassLoader.class.getDeclaredField(name);
            }else {
                f = obj.getClass().getDeclaredField(name);
            }
            f.setAccessible(true);
            return f.get(obj);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
