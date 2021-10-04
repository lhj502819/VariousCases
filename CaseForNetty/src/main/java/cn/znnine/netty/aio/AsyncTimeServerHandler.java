package cn.znnine.netty.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 异步的时间服务器处理类
 *
 * @author lihongjian
 * @since 2021/9/25
 */
@Slf4j
public class AsyncTimeServerHandler implements Runnable{

    private int port;

    CountDownLatch latch;

    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            log.info("The time server is startr in port : " + port);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        /**
         * 用于接收客户端的链接，由于是异步操作，我们可以传递一个CompletionHandler<AsynchronousSocketChannel,? super A> 类型的hander实例
         *  接收accept操作成功的通知消息
         *
         *  调用AsynchronousServerSocketChannel#accept后，如果有新的客户端连接接入，系统将回调CompletionHandler的completed方法，失败则回调failed方法
         */
        asynchronousServerSocketChannel.accept(this , new AcceptCompletionHandler());
    }
}
