package cn.znnine.netty.nio.netty.v1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lihongjian
 * @since 2021/10/5
 */
@Slf4j
public class NettyTimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMessage;

    public NettyTimeClientHandler(){
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    /**
     * 当客户端和服务端TCP链路建立成功后，Netty的NIO线程会调用channelActive方法，发送查询时间的指令给服务端
     * 调用ChannelHandlerContext的writeAndFlush方法将请求消息发送给服务端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    /**
     * 当服务端返回应答消息后时，channelRead方法被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is :" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("Unexpected exception from downstream : "+ cause.getMessage());
        ctx.close();
    }
}
