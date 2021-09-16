package cn.onenine.jvm.classloader;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 运行期加载类，使用URLClassLoader#addUrl
 *
 * @author lihongjian
 * @since 2021/8/29
 */
public class JVMAppClassLoaderAddURL {
    public static void main(String[] args) {
        URLClassLoader classLoader = (URLClassLoader) JVMAppClassLoaderAddURL.class.getClassLoader();
        String appPath = "file:/E:/workspeace/VariousCases/src/main/java/cn/onenine/jvm/classloader/customclassloader/test1";
        try {
            Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);
            URL url = new URL(appPath);
            addURL.invoke(classLoader,url);
            //初始化Hello类
            Class.forName("cn.onenine.jvm.classloader.customclassloader.test1.Hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
