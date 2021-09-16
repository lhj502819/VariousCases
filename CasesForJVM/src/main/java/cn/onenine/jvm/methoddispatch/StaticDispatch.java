package cn.onenine.jvm.methoddispatch;

/**
 * Description：静态方法分派
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/9/3
 */
public class StaticDispatch {

    static abstract class Human {

    }

    static class Man extends Human {

    }

    static class Woman extends Human {

    }

    public void sayHello(Human guy) {
        System.out.println("hello,guy");
    }

    public void sayHello(Man guy) {
        System.out.println("hello,Man");
    }

    public void sayHello(Woman guy) {
        System.out.println("hello,Woman");
    }

    public static void main(String[] args) {
        Human woman = new Woman();
        Human man = new Man();
        StaticDispatch staticDispatch = new StaticDispatch();
        staticDispatch.sayHello(woman);
        staticDispatch.sayHello(man);
    }

}
