package cn.znnine.netty.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 伪异步I/O Client
 *
 * @author lihongjian
 * @since 2021/9/16
 */
@Slf4j
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1" , port) , "TimeClientHandle-001").start();
    }
}
