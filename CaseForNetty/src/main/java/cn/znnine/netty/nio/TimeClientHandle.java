package cn.znnine.netty.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Description：处理异步连接和读写操作
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/9/24
 */
@Slf4j
public class TimeClientHandle implements Runnable {

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop;

    public TimeClientHandle(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            //创建Reactor线程，创建多路复用器并启动线程
            selector = Selector.open();
            //打开ServerSocketChannel，用于监听客户端的连接，它是所有客户端连接的父管道，是SelectableChannel（负责网络读写）的子类
            socketChannel = SocketChannel.open();
            //设置为非阻塞
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            log.error("[TimeClientHandle#TimeClientHandle]", e);
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            log.error("[TimeClientHandle#run]", e);
            System.exit(1);
        }
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()){
                    SelectionKey nextSelectKey = it.next();
                    it.remove();
                    try {
                        handleInput(nextSelectKey);
                    }catch (Exception e){
                        if(nextSelectKey !=null){
                            nextSelectKey.cancel();
                            if(nextSelectKey.channel() != null){
                                nextSelectKey.channel().close();
                            }
                        }
                    }
                }
            }catch (Exception e){
                log.error("[TimeClientHandle#run]", e);
                System.exit(1);
            }
        }
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleInput(SelectionKey key) throws Exception{
        if(key.isValid()){
            //判断是否连接成功
            SocketChannel sc = (SocketChannel) key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }else {
                    System.exit(1);
                }
            }
            if(key.isReadable()){
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new java.lang.String(bytes, "UTF-8");
                    System.out.println("Now is : "+ body);
                    stop = true;
                }else if(readBytes < 0){
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                }else {
                    ; //读到0字节。忽略
                }
            }
        }

    }


    private void doConnect() throws Exception{
        //如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector , SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else {
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel sc) throws Exception{
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }
    }
}
