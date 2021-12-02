package cn.znnine.netty.nio.java.demo;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入到Buffer时，可以采用buffer数组，依次写入
 * Gathering：从Buffer读取数据时，可以采用buffer数组，依次读
 *
 * @author lihongjian
 * @since 2021/12/2
 */
public class ScatteringAndGatheringDemo {
    public static void main(String[] args) throws Exception {
        //使用ServerSocketChannel和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress(8999);
        //绑定端口
        serverSocketChannel.socket().bind(socketAddress);

        //创建Buffer数组
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(2);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        //假设从客户端读8个字节
        int messageLength = 7;
        //假设从客户端读取
        while (true) {
            int bytesRead = 0;
            while (bytesRead < messageLength) {
                long read = socketChannel.read(buffers);
                //累计读取的字节数
                bytesRead += read;
                //打印buffer的position和limit
                Arrays.stream(buffers).map(buffer -> "position:" + buffer.position() + ",limit:" + buffer.limit()).forEach(System.out::println);
            }
            //读取完毕，将buffer flip
            Arrays.stream(buffers).forEach(Buffer::flip);
            //将数据从Buffer读出来写回给客户端
            long bytesWrite = 0;
            while (bytesWrite < messageLength) {
                long write = socketChannel.write(buffers);
                bytesWrite += write;
            }

            //将所有的Buffer clear
            Arrays.stream(buffers).forEach(ByteBuffer::clear);

            System.out.println("byteRead=" + bytesRead  + " byteWrite=" +bytesWrite + ",messageLength=" + messageLength);
        }


    }
}
