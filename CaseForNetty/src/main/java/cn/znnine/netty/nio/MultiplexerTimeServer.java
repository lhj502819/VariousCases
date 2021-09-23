package cn.znnine.netty.nio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用类，是一个独立的线程，负责轮询多路复用器Selector，可以处理多个客户端的并发接入
 *
 * @author lihongjian
 * @since 2021/9/23
 */
@Slf4j
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            //创建Reactor线程，创建多路复用器并启动线程
            selector = Selector.open();
            //打开ServerSocketChannel，用于监听客户端的连接，它是所有客户端连接的父管道，是SelectableChannel（负责网络读写）的子类
            serverChannel = ServerSocketChannel.open();
            //设置Channel为非阻塞模式
            serverChannel.configureBlocking(false);
            //绑定端口
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            //将Channel注册到selector上
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            log.error("[MultiplexerTimeServer#MultiplexerTimeServer]", e);
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }


    @Override
    public void run() {
        try {
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            SelectionKey selectionKey = null;
            while (iterator.hasNext()) {
                selectionKey = iterator.next();
                iterator.remove();
                try {
                    handleInput(selectionKey);
                } catch (Exception e) {
                    if (selectionKey != null) {
                        selectionKey.cancel();
                        if (selectionKey.channel() != null) {
                            selectionKey.channel().close();
                        }
                    }
                }
            }
            //多路复用器关闭后，所有注册在上边的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
            if (selector != null) {
                try {
                    selector.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error("#run", e);
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理新接入的请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //将Channel添加到selector,key为OP_READ，读操作
                sc.register(selector, SelectionKey.OP_READ);
            }
            //如果可读
            if (key.isReadable()) {
                //Read the data
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    log.info("The time server receive order：" + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                            ? new Date(System.currentTimeMillis()).toString()
                            : "BAD ORDER";
                    doWrite(sc,currentTime);
                }else if(readBytes < 0){
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                }else {
                    //读到0字节，忽略
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException{
        if(StringUtils.hasText(response)){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
