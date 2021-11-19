package cn.znnine.netty.protocol.http.xml.client;

import cn.znnine.netty.protocol.http.xml.HttpXmlRequestEncoder;
import cn.znnine.netty.protocol.http.xml.HttpXmlResponseDecoder;
import cn.znnine.netty.protocol.http.xml.HttpXmlResponseEncoder;
import cn.znnine.netty.protocol.http.xml.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.InetSocketAddress;

/**
 * 客户端
 *
 * @author lihongjian
 * @since 2021/11/17
 */
public class HttpXmlClient {
    public void connect(int port)throws Exception{
        //配置服务端的NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //XML解码器
                            ch.pipeline().addLast("http-decoder" , new HttpResponseDecoder());
                            //负责将1个HTTP请求消息的多个部分合并成一条完整的HTTP消息
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast(
                                    "xml-decoder" , new HttpXmlResponseDecoder(Order.class,true));
                            ch.pipeline().addLast("http-encoder",new HttpRequestEncoder());
                            //XML编码器
                            ch.pipeline().addLast("xml-encoder" , new HttpXmlRequestEncoder());
                            ch.pipeline().addLast("xmlClientHandler" , new HttpXmlClientHandle());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.connect(new InetSocketAddress(port)).sync();

            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                //None  采用默认值
            }
        }
        new HttpXmlClient().connect(port);
    }
}
