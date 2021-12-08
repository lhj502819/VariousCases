package cn.znnine.netty.nio.java.simple;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端
 *
 * @author lihongjian
 * @since 2021/12/8
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        //创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定6666端口在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到Selector上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){

            //等待1s 如果没有事件发生则返回
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            /**
             * 如果大于0，就获取相关的selectionKey集合
             * 如果大于0，表示已经获取到关注的事件
             */
            //通过selector.selected获取关注事件的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                //获取到selectionKey
                SelectionKey key = iterator.next();
                //根据key对应的通道发生的事件做对应的处理
                if(key.isAcceptable()){
                    //如果是OP_ACCEPT，表示有新的客户端连接
                    //该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成 生成了一个socketChannel");
                    //将SocketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                //如果是发生OP_READ
                if(key.isReadable()){
                    //通过key反向获取到channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该Channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端 " + new String(buffer.array()));
                }

                //手动从集合中移出当前selectionKey，防止重复操作
                iterator.remove();
            }
        }

    }
}
