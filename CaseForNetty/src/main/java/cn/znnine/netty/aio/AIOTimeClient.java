package cn.znnine.netty.aio;

import lombok.extern.slf4j.Slf4j;

/**
 * 伪异步I/O Client
 *
 * @author lihongjian
 * @since 2021/9/16
 */
@Slf4j
public class AIOTimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AsyncTimeClientHandler-001").start();
    }
}
