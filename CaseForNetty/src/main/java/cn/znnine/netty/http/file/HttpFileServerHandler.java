package cn.znnine.netty.http.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Description：文件服务器的业务处理逻辑
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/11/1
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    public HttpFileServerHandler(String url) {

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

    }
}
