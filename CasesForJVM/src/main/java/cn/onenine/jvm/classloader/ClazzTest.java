package cn.onenine.jvm.classloader;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/8/27
 */
public class ClazzTest {

    public static void main(String[] args) {
        /**
         * 判断调用者Class是否为传入Class的父类或者当前类，是则返回true
         */
        System.out.println(Animal.class.isAssignableFrom(Bird.class));

        /**
         * 判断传入对象是否为调用者的实例
         */
        Bird bird = new Bird();
        System.out.println(Bird.class.isInstance(bird));

    }
}


class Animal{

}

class Bird extends Animal{

}

interface Fish{

}
