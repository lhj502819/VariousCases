package cn.onenine.effective.thread;

import java.util.concurrent.TimeUnit;

/**
 * Description：线程终止Demo
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/9/7
 */
public class StopThread {

    private static boolean stopRequested;

    public static void main(String[] args) throws Throwable {
        Thread thread = new Thread(() -> {
            int i = 0;
            /**
             * 由于JIT即使编译，将我们的成语优化为类似下边的语句，避免每次循环进行边界检查
             * if(!stopRequested){
             *     while(true){
             *      i++;
             *     }
             * }
             *
             * 可以通过-Djava.compiler=NONE来关闭
             */
            while (!stopRequested) {
                i++;
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }

}
