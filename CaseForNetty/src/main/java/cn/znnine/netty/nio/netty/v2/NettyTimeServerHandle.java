package cn.znnine.netty.nio.netty.v2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * 用于对网络事件进行操作读写
 * @author lihongjian
 * @since 2021/10/4
 */
public class NettyTimeServerHandle extends ChannelHandlerAdapter {


    private int counter ;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        //buf.readableBytes()可以获取缓冲区可读的字节数
        byte[] req = new byte[buf.readableBytes()];
        //通过readBytes方法将缓冲区中的字节数组复制到新建的byte数组中，最后通过new String()构造函数获取请求消息
        buf.readBytes(req);
//        String body = new String(req, "UTF-8");
        //改造，模拟拆包、粘包，每读到一条消息后，就计一次数，然后发送应答消息给客户端，按照设计，服务端接收到的消息总数应该跟客户端发送的消息总数相同
        //而且请求消息删除回车换行符后应该为“QUERY TIME ORDER”
        String body = new String(req, "UTF-8").substring(0,req.length - System.getProperty("line.separator").length());
        System.out.println("The time server receive order ：" + body + "; the counter is ：" + ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                ? new Date(System.currentTimeMillis()).toString()
                : "BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        //异步发送应答消息给客户端
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将消息发送队列中的消息写入到SocketChannel中发送给对方
        //从性能角度考虑，为了防止频繁地唤醒Selector进行消息发送，Netty的write方法并不直接将消息写入SocketChannel中，
        //调用write方法只是把待发送的消息放到缓冲数组中，再通过调用flush方法，将发送缓冲区中的消息全部写入到SocketChannel中
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发送异常时关闭ChannelHandlerContext，释放和ChannelHandlerContext的相关联的句柄等资源
        ctx.close();
    }
}
