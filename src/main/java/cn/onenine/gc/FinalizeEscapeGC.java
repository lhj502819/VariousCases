package cn.onenine.gc;

/**
 * 测试finalize方法
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
