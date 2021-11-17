package cn.znnine.netty.protocol.http.xml;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author lihongjian
 * @since 2021/11/17
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getResult());
        FullHttpResponse response = msg.getHttpResponse();
        //如果业务侧已经构造了HTTP应答消息，则利用业务已有的应答消息重新复制一个新的HTTP应答消息
        //无法重用业务侧的HTTP应答消息的主要原因是：Netty的DefaultFullHttpResponse没有提供动态设置消息体content的接口
        // 只能在第一次构造的时候设置内容
        if (response == null) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK, body);
        } else {
            response = new DefaultFullHttpResponse(
                    msg.getHttpResponse().getProtocolVersion(),
                    msg.getHttpResponse().getStatus(),
                    body);
        }
        response.headers().set(CONTENT_TYPE,"text/xml");
        setContentLength(response,body.readableBytes());
        out.add(response);
    }
}
