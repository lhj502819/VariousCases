package cn.znnine.netty.protocol.http.xml;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

/**
 * Description：
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/11/12
 */
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest>{

    protected HttpXmlRequestDecoder(Class<?> clazz) {
        super(clazz);
    }

    public HttpXmlRequestDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        //返回客户端错误信息
        if(!msg.getDecoderResult().isSuccess()){
            sendError(ctx,BAD_REQUEST);
        }
        HttpXmlRequest request = new HttpXmlRequest(msg, decode0(ctx, msg.content()));
        //将请求交给一个解码器处理
        out.add(request);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status,
                Unpooled.copiedBuffer("Failure：" + status.toString() +  "\r\n" , CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
