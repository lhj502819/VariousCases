package cn.znnine.netty.protocol.priv.server;

import cn.znnine.netty.protocol.priv.NettyConstant;
import cn.znnine.netty.protocol.priv.codec.NettyMessageDecoder;
import cn.znnine.netty.protocol.priv.codec.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 服务端，主要工作就是握手的接入认证等，不用关心断连重连等事件
 *
 * @author lihongjian
 * @since 2021/11/24
 */
public class NettyServer {

    public void bind()throws Exception{
        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024 ,4,4));
                        ch.pipeline().addLast(new NettyMessageEncoder());
                        ch.pipeline().addLast("readTimeoutHandler" , new ReadTimeoutHandler(50));
                        ch.pipeline().addLast(new LoginAuthRespHandler());
                        ch.pipeline().addLast("HeartBeatRespHandler" , new HeartBeatRespHandler());
                    }
                });
        //绑定端口，同步等待成功
        b.bind(NettyConstant.REMOTEIP,NettyConstant.PORT).sync();
        System.out.println("Netty server start ok：" + NettyConstant.REMOTEIP + ":" + NettyConstant.PORT);
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }
}
