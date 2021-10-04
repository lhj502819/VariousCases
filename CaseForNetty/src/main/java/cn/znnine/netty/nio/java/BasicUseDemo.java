package cn.znnine.netty.nio.java;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Description：Java NIO 使用Demo
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/9/17
 */
public class BasicUseDemo {

    /**
     * Reactor
     * @param args
     * @throws Exception
     */
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
        //1、打开ServerSocketChannel，用于监听客户端的连接，它是所有客户端连接的父管道，是SelectableChannel（负责网络读写）的子类
        ServerSocketChannel acceptServer = ServerSocketChannel.open();
        //2、绑定监听端口，设置连接为非阻塞模式
        acceptServer.socket().bind(new InetSocketAddress(InetAddress.getByName("IP"),port));
        acceptServer.configureBlocking(false);
        //创建Reactor线程，创建多路复用器并启动线程，示例代码如下
        Selector selector = Selector.open();
//        new Thread(new ReactorTask()).start();
        //将ServerSocketChannel注册到Reactor线程的多路复用器Selector上，监听ACCEPT事件
        SelectionKey key = acceptServer.register(selector, SelectionKey.OP_ACCEPT);
        //多路复用器在线程run方法的无限循环体内轮询准备就绪的Key
        int num = selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> it = selectionKeys.iterator();
        while (it.hasNext()){
            SelectionKey selectionKey = it.next();
            //deal with I/O event ...
        }
        //多路复用器监听到有新的客户端接入，处理新的接入请求，完成TCP三次握手，建立物理链路，示例代码如下
        SocketChannel channel = acceptServer.accept();
        //设置客户端链路为非阻塞模式
        channel.configureBlocking(false);
        channel.socket().setReuseAddress(true);
        //将新接入的客户端连接注册到Reactor线程的多路复用器上，监听读操作，读取客户端发送的网格消息
        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
        //异步读取客户端请求消息到缓冲器
//        int readNumber = channel.register(receivedBuffer);
        //对ByteBuffer进行编解码，如果有半包消息指针reset，继续读取后续的报文，将解码成功的消息封装成Task，投递到业务线程中，进行业务逻辑编排
        Object message = null;
//        while (buffer.hasRemain()){
//          //.....
//        }
        //将POJO对象encode成ByteBuffer，调用SocketChannel的异步write接口，将消息异步发送给客户端
//        channel.write(buffer);


        /**
         * 注意：如果发送区TCP缓冲区满，会导致写半包，此时需要注册监听写操作位，循环写，直到整包消息写入TCP缓冲区。
         */

    }
}
