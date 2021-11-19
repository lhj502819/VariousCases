package cn.znnine.netty.protocol.http.xml.server;

import cn.znnine.netty.protocol.http.xml.HttpXmlRequestDecoder;
import cn.znnine.netty.protocol.http.xml.HttpXmlResponseEncoder;
import cn.znnine.netty.protocol.http.xml.pojo.Order;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * 以商品订购程序作为代码学习Netty的Protobuf服务端的开发
 *
 * @author lihongjian
 * @since 2021/10/23
 */
public class HttpXmlServer {
    public void bind(int port) throws InterruptedException {
        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup ,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //负责将1个HTTP请求消息的多个部分合并成一条完整的HTTP消息
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                            ch.pipeline().addLast("xml-decoder" , new HttpXmlRequestDecoder(Order.class,true));
                            ch.pipeline().addLast("http-encoder" , new HttpResponseEncoder());
                            ch.pipeline().addLast("xml-encoder" , new HttpXmlResponseEncoder());
                            ch.pipeline().addLast("xmlServerHandler" , new HttpXmlServerHandler());

                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(new InetSocketAddress(port)).sync();
            System.out.println("HTTP订购服务器启动，网址是 : " + "http://localhost:"
                    + port);
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length >0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        new HttpXmlServer().bind(port);
    }
}
