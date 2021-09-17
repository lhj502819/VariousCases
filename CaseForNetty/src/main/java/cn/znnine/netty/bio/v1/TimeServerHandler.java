package cn.znnine.netty.bio.v1;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author lihongjian
 * @since 2021/9/16
 */
@Slf4j
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if(body == null){
                    break;
                }
                log.info("The time server receive order : {}" , body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                        ? new Date(System.currentTimeMillis()).toString()
                        : "BAD ORDER";
                out.println(currentTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                }catch (IOException exception){
                    exception.printStackTrace();
                }
            }

            if(out != null){
                out.close();
                out = null;
            }

            if(this.socket != null){
                try {
                    socket.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
