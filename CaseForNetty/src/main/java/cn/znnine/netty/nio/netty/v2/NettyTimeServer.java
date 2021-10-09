package cn.znnine.netty.nio.netty.v2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 基于Netty的TimeServer服务器
 *
 * @author lihongjian
 * @since 2021/10/4
 */
public class NettyTimeServer {

    public void bind(int port) throws Exception {
        //配置服务端的NIO线程组，包含了一组NIO线程，专门用于网络事件的处理，实际上就是一组Reactor线程组
        //一个用于客户端连接的处理
        //一个用于进行SocketChannel的网络读写
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    /**
                     * SO_BACKLOG对应着TCP的backlog，在TCP建立连接阶段，内核维护着两个队列：
                     *   1、未完成队列：正处于三次握手当中，客户端发送SYN过来，
                     *   服务端回应SYN+ACK之后，服务端当前处于SYN_RECV状态，此时连接在未完成队列中
                     *   2、已完成队列：已完成三次握手，客户端回应ACK之后，两边都处于ESTABLISHED，
                     *   此时连接从未完成队列移到已完成队列中，此时服务端调用accept，就从已完成队列移出并返回新创建的socketfd
                     *
                     *  backlog就是已完成队列的大小
                     */
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            //绑定端口，同步等待成功
            //ChannelFuture主要用于异步操作的通知回调
            ChannelFuture f = b.bind(port).sync();
            //等待服务器端监听端口关闭
            //使用sync()方法进行阻塞，等待服务端链路关闭后main函数才退出
            f.channel().closeFuture().sync();
        } finally {
            //优雅退出，释放x线程池资源（会释放和shutdownGracefully相关联的资源）
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    /**
     * 作用类似于Reactor模式中的Handler类，主要用于处理I/O事件，例如记录日志、对消息进行编解码等。
     */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyTimeServerHandle());
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if (args != null && args.length >0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        new NettyTimeServer().bind(port);
    }
}
