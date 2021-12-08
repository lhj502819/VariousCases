package cn.znnine.netty.nio.java.simple;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 客户端
 *
 * @author lihongjian
 * @since 2021/12/8
 */
public class NioClient {

    public static void main(String[] args) throws Exception{
        //创建一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //设置服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            //如果连接不成功
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
            //如果连接成功，就发送数据
            String msg = "hello 零壹玖";
            //包装成ByteBuffer
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
            //发送数据，将buffer数据写入channel
            socketChannel.write(buffer);
            System.in.read();
        }


    }

}
