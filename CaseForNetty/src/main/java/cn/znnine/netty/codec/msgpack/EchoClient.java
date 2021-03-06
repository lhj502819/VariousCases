package cn.znnine.netty.codec.msgpack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author lihongjian
 * @since 2021/10/12
 */
public class EchoClient {
    public void connect(int port, String host) throws Exception {
        //配置客户端NIO线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 当创建NioSocketChannel成功之后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中
                         * 用于处理网络I/O事件
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //通过在Msgpack解码器之前增加LengthFieldBasedFrameDecoder用于处理半包消息
                            //在MessagePack编码器之前增加LengthFieldPrepender，它将在ByteBuf之前增加2个字节的消息长度字段
                            //LengthFieldPrepender将会在ByteBuf之前增加2个字节的消息长度字段
                            //
                            ch.pipeline().addLast("frameDecoder",
                                    new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                            ch.pipeline().addLast("decoder" , new MsgpackDecoder());
                            ch.pipeline().addLast("frameEncoder" , new LengthFieldPrepender(2));
                            ch.pipeline().addLast("encoder" , new MsgpackEncoder());
                            ch.pipeline().addLast(new EchoClientHandler(1000));
                        }
                    });
            //发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            //优雅退出，释放NIO线程组
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
        new EchoClient().connect(port, "127.0.0.1");
    }
}
