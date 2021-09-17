package cn.znnine.netty.nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Description：Java NIO 使用Demo
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/9/17
 */
public class BasicUseDemo {

    public static void main(String[] args) throws Exception{
        int port = 8080;
        /**
         * Channel是一个通道，就像自来水管，网络数据通过Channel读取和写入
         * 通道与流的不同之处在于通道是双向的，流只是在一个方向上移动（一个流必须是InputStream或者OutputStream的子类）
         * 而通道可以用于读、写或者二者同时进行
         *
         * Channel是一个接口，子类可以分为两大类：用于网络读写的SelectableChannel和用于文件操作的FileChannel
         * 下边的ServerSocketChannel和SocketChannel都是SelectableChannel的子类
         */
        //1、打开ServerSocketChannel，用于监听客户端的连接，它是所有客户端连接的父管道
        ServerSocketChannel acceptServer = ServerSocketChannel.open();
        //2、绑定监听端口，设置连接为非阻塞模式
        acceptServer.socket().bind(new InetSocketAddress(InetAddress.getByName("IP"),port));
        acceptServer.configureBlocking(false);
        //创建Reactor线程，创建多路复用器并启动线程，示例代码如下
        Selector selector = Selector.open();
//        new Thread(new ReactorTask()).start();
        SelectionKey.OP_ACCEPT

    }
}
