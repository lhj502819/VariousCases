package cn.znnine.netty.http.file;

import cn.znnine.netty.codec.marshalling.SubReqServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Description：Http文件服务器  服务端
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/11/1
 */
public class HttpFileServer {

    private static final String DEFAULT_URL = "/src/main/java/cn/znnine/netty/http/file";

    public void run(final int port , final String url) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //HTTP请求消息解码器
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //HttpObjectAggregator作用是将多个消息转换为单一的FullHttpRequest或者FullHttpResponse
                            //  原因是HTTP解码器在每个HTTP消息中会生成多个消息对象
                            ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            //Http响应编码器
                            ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            //Chunked handler的主要作用是支持异步发送大的码流（例如大文件的传输），但不占用过多的内存，防止发生Java内存溢出错误
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            //HttpFileServerHandler用于文件服务器的业务处理逻辑
                            ch.pipeline().addLast("fileServerHandler" , new HttpFileServerHandler(url));
                        }

                    });
            ChannelFuture future = b.bind("127.0.0.1", port).sync();
            System.out.println("HTTP 文件目录服务器启动，网址是：" +"http://127.0.0.1:" + port +url);
            future.channel().closeFuture().sync();
        } finally {
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
        String url = DEFAULT_URL;
        new HttpFileServer().run(port,url);
    }

}
