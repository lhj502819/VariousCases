package cn.znnine.netty.protocol.http.xml.client;

import cn.znnine.netty.protocol.http.xml.HttpXmlRequest;
import cn.znnine.netty.protocol.http.xml.HttpXmlResponse;
import cn.znnine.netty.protocol.http.xml.pojo.OrderFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * TODO
 *
 * @author lihongjian
 * @since 2021/11/17
 */
public class HttpXmlClientHandle extends SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(123));
        ctx.writeAndFlush(request);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("The client receive response of http header is ：" + msg.getHttpResponse().headers().names());

        System.out.println("The client receive response of http body is ：" + msg.getResult());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}