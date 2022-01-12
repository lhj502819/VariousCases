package cn.znnine.netty.nio.java.scalability;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 经典服务网络服务设计
 *
 * @author lihongjian
 * @since 2022/1/9
 */
public class Server implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (!Thread.interrupted()) {
                new Thread(new Handler(serverSocket.accept())).start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    static class Handler implements Runnable {
        final Socket socket;
        public Handler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                byte[] input = new byte[1024];
                socket.getInputStream().read(input);
                byte[] output = process(input);
                socket.getOutputStream().write(output);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        private byte[] process(byte[] cmd) {
            return null;
        }
    }
}
