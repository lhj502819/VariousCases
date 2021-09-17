package cn.znnine.netty.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞I/O
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
            /**
             * 通过一个无限循环来监听客户端的连接，如果没有客户端接入，则主线程阻塞在ServerSocket的accept上
             * 启动后，通过JVisual VM打印线程堆栈，我们可以发现主程序确实阻塞在accept操作上，如图2-2所示：
             */
            while (true){
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
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


