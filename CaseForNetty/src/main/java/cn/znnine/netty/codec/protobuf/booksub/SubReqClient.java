package cn.znnine.netty.codec.protobuf.booksub;

import cn.znnine.netty.codec.protobuf.demo.SubscribeRespProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author lihongjian
 * @since 2021/10/23
 */
public class SubReqClient {
    public void connect(int port, String host)throws Exception{
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
                                //ProtobufVarint32FrameDecoder主要用于半包处理
                                ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                                //ProtobufDecoder解码器，参数是com.google.protobuf.MessageLite，实际上就是要高速ProtobufDecoder需要解码的目标类是什么
                                //否则仅仅从字节数组中是无法判断出要解码的目标类型信息的
                                //使用Server端的响应实例做入参
                                ch.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
                                ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                                ch.pipeline().addLast(new ProtobufEncoder());
                                ch.pipeline().addLast(new SubReqClientHandler());
                            }
                        });
                //绑定端口，同步等待成功
                ChannelFuture f = b.connect(host,port).sync();

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
            new SubReqClient().connect(port, "127.0.0.1");
    }
}
