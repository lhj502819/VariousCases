package cn.znnine.netty.nio;

/**
 * Java NIO Server
 *
 * @author lihongjian
 * @since 2021/9/23
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length >0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);

        new Thread(timeServer,"NIO-MutiplexerTimeServer").start();


    }
}
