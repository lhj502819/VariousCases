package cn.znnine.jvm.classloader.customclassloader.test1;

import java.io.FileInputStream;
import java.util.Base64;

/**
 * 将Hello.class使用Base64加密，通过自定义类加载器重新解密加载
 *
 * @author lihongjian
 * @since 2021/8/28
 */
public class LoadHelloClassEncodingStrClassLoader {
    /**
     * 加密后的内容
     */
    private static final String helloClassBase64EncodedStr =
            "yv66vgAAADQAHwoABgARCQASABMIABQKABUAFgcAFwcAGAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQA6TGNuL29uZW5pbmUvanZtL2NsYXNzbG9hZGVyL2N1c3RvbWNsYXNzbG9hZGVyL3Rlc3QxL0hlbGxvOwEACDxjbGluaXQ+AQAKU291cmNlRmlsZQEACkhlbGxvLmphdmEMAAcACAcAGQwAGgAbAQAVSGVsbG8gQ2xhc3MgSW5pdC4uLi4uBwAcDAAdAB4BADhjbi9vbmVuaW5lL2p2bS9jbGFzc2xvYWRlci9jdXN0b21jbGFzc2xvYWRlci90ZXN0MS9IZWxsbwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYAIQAFAAYAAAAAAAIAAQAHAAgAAQAJAAAALwABAAEAAAAFKrcAAbEAAAACAAoAAAAGAAEAAAAJAAsAAAAMAAEAAAAFAAwADQAAAAgADgAIAAEACQAAACUAAgAAAAAACbIAAhIDtgAEsQAAAAEACgAAAAoAAgAAAAsACAAMAAEADwAAAAIAEA==";

    public static void main(String[] args) throws Exception {
        ClassLoader customClassLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) {
                byte[] helloClassByteCode = new byte[0];
                try {
                    helloClassByteCode = Base64.getDecoder()
                            .decode(decodeHelloClass("E:\\workspeace\\VariousCases\\src\\main\\java\\cn\\onenine\\jvm\\classloader\\customclassloader\\test1\\Hello.class"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return defineClass(name, helloClassByteCode, 0, helloClassByteCode.length);
            }
        };
        Class<?> helloClass = customClassLoader
                .loadClass("cn.znnine.jvm.classloader.customclassloader.test1.Hello");
        helloClass.newInstance();
    }


    /**
     * 对指定Class文件进行Base64加密
     */
    private static String decodeHelloClass(String path) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(path);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
