package cn.onenine.jvm.object;

import org.openjdk.jol.info.ClassLayout;

/**
 * 对象内存布局分析
 *   在64位JVM中，对象头占据的空间是12字节 = 96bit = 64+32 ，但是以8字节对齐，所以一个空类的实例至少占用16字节。
 *   在32位JVM中，对象头占8个字节，以4的倍数对齐（32=4*8）
 *  所以new出来的简单对象，就算一个空对象，都会占不少字节
 *
 * 通常在32位JVM中，以及内存小于-Xmx32G的64位JVM上（默认开启指针压缩），一个引用占用的内存默认是4个字节
 *
 * 包装类型比原生类型消耗的内存要多：
 *   Integer：占用16字节(头部8+4=12，数据4字节)，int部分占4字节，所以Integer比原生类型int要多消耗300%的内存
 *
 *   java.lang.Integer object internals:
 *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
 *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
 *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
 *       8     4        (object header)                           ae 22 00 f8 (10101110 00100010 00000000 11111000) (-134208850)
 *      12     4    int Integer.value                             156
 * Instance size: 16 bytes
 *
 *
 * @author lihongjian
 * @since 2021/9/11
 */
public class ObjectAnalysis {
    public static void main(String[] args) {
        User user = new User("小明" , 156 , 156);
        System.out.println(ClassLayout.parseInstance(user).toPrintable());


        Integer num = 156;
        System.out.println(ClassLayout.parseInstance(num) .toPrintable());

        int num2 = 156;
        System.out.println(ClassLayout.parseInstance(num2) .toPrintable());

        Long num3 = 1L;
        System.out.println(ClassLayout.parseInstance(num3) .toPrintable());
    }
}

class User{
    String name;

    int age1;

    Integer age;

    public User(String name, int age1, Integer age) {
        this.name = name;
        this.age1 = age1;
        this.age = age;
    }
}
