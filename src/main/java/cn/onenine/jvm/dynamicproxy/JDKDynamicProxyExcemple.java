package cn.onenine.jvm.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description：JDK动态代理
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/9/3
 */
public class JDKDynamicProxyExcemple {

    interface IHello{
        void sayHello();
    }

    static class Hello implements IHello{
        @Override
        public void sayHello() {
            System.out.println("hello world");
        }
    }

    static class DynamicProxy implements InvocationHandler{

        Object object;

        Object bind(Object object){
            this.object = object;
            return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(),this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("welcome");
            return method.invoke(object,args);
        }
    }


    public static void main(String[] args) {
        Hello hello = new Hello();
        DynamicProxy dynamicProxy = new DynamicProxy();
        IHello hello1 = (IHello)dynamicProxy.bind(hello);
        hello1.sayHello();
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    }
}
