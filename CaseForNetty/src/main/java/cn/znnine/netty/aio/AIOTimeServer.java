package cn.znnine.netty.aio;

/**
 * AIO时间服务器服务端
 *
 * @author lihongjian
 * @since 2021/9/25
 */
public class AIOTimeServer {
    public static void   main(String[] args) {
        int port = 8080;
        if (args != null && args.length >0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
