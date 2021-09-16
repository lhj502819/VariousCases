package cn.onenine.netty;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单线程Socket进程
 *
 * @author lihongjian
 * @since 2021/9/15
 */
public class HttpServer {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ServerSocket serverSocket = new ServerSocket(8001);
        while (true){
            try {
                Socket socket = serverSocket.accept();
                executorService.submit(()->{
                    service(socket);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private static void service(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
        }catch (Exception e){

        }
    }
}


