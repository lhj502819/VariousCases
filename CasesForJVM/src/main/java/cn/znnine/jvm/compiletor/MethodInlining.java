package cn.znnine.jvm.compiletor;

/**
 * 方法内联
 *
 * @author lihongjian
 * @since 2021/9/4
 */
public class MethodInlining {
    public static void foo(Object obj){
        if(obj == null){
            System.out.println("do....");
        }
    }
    public static void testInline(String[] args){
        Object object = null;
        foo(object);
    }

    public static void main(String[] args) throws Exception{
        testInline(args);
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
