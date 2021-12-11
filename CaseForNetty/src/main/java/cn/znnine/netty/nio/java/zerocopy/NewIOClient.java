package cn.znnine.netty.nio.java.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author lihongjian
 * @since 2021/12/11
 */
public class NewIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        String filename = "E:\\File\\Netty\\尚硅谷\\Netty相关资料\\资料\\protoc-3.6.1-win32.zip";

        //得到一个文件Channel
        FileChannel fileChannel = new FileInputStream(filename).getChannel();

        //得到一个文件Channel
        long startTime = System.currentTimeMillis();
        //在Linux下使用一次transferTo方法就可以完成传输
        //在windows下一次调用transferTo只能发送8m，就需要分段传输文件
        //transferTo底层使用零拷贝
        long transfer = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总字节数=" + transfer + " 耗时：" + (System.currentTimeMillis() - startTime));
    }
}
