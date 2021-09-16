package cn.onenine.jvm.gc;

/**
 * 测试finalize方法
 *
 * 类重写了finalize()并在方法中将自身赋值给了类静态变量，
 * 执行完等待虚拟机再次执行标记时，发现当前对象已经存在强引用了，那么就不会回收此对象了。
 * 下边有两段相同的代码，但是第二次显示并没有执行finalize()方法，说明了finalize()方法在一个对象中只能被执行一次。
 *
 * @author lihongjian
 * @since 2021/7/19
 */
public class FinalizeEscapeGC {

    public static FinalizeEscapeGC SAVE_HOKE = null;

    public void isAlive(){
        System.out.println("yes , i am still alive");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("FinalizeEscapeGC#finalize方法被执行了");
        FinalizeEscapeGC.SAVE_HOKE = this;
    }

    public static void main(String[] args) throws Exception{
        SAVE_HOKE = new FinalizeEscapeGC();
        SAVE_HOKE = null;
        //对象救自己
        System.gc();
        Thread.sleep(500);
        if(SAVE_HOKE != null){
            SAVE_HOKE.isAlive();
        }else {
            System.out.println("SAVE_HOKE is dead");
        }

        SAVE_HOKE = null;
        //对象救自己
        System.gc();
        Thread.sleep(500);
        if(SAVE_HOKE != null){
            SAVE_HOKE.isAlive();
        }else {
            System.out.println("SAVE_HOKE is dead");
        }

    }
}
