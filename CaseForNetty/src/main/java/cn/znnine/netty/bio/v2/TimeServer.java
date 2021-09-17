package cn.znnine.netty.bio.v2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 伪异步阻塞I/O
 *
 * 采用线程池和任务队列可以实现一种伪异步的I/O通信框架，当有新的客户端接入时，将客户端的Socket封装成一个Task投递到后端的线程池中处理
 * 由于线程池可以设置消息队列的大小和最大线程数，因此它的资源占用是可控的，无论多少个客户端并发访问，都不会导致资源的耗尽和宕机
 *
 * @author lihongjian
 * @since 2021/9/16
 */
@Slf4j
public class TimeServer {
    public static void main(String[] args) throws IOException{
        int port = 8080;
        if (args != null && args.length >0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            log.info("The timeServer is start in port : {}" , port);
            Socket socket = null;

            //创建I/O任务线程池
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50,10000);

            /**
             * 通过一个无限循环来监听客户端的连接，如果没有客户端接入，则主线程阻塞在ServerSocket的accept上
             * 启动后，通过JVisual VM打印线程堆栈，我们可以发现主程序确实阻塞在accept操作上，如图2-2所示：
             */
            while (true){
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        }finally {
            if(server != null) {
                server.close();
                log.warn("The timeServer is closed");
                server = null;
            }
        }
    }
}


