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
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest> {

    /**
     * @param clazz 需要解码的对象的类型信息
     */
    protected HttpXmlRequestDecoder(Class<?> clazz) {
        //码流打印开关默认关闭
        this(clazz, false);
    }

    /**
     * @param clazz   需要解码的对象的类型信息
     * @param isPrint 是否打印HTTP消息体码流的开关
     */
    public HttpXmlRequestDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        //对HTTP请求消息本身的解码结果进行判断，如果已经解码失败，则返回客户端错误信息
        if (!msg.getDecoderResult().isSuccess()) {
            //公司项目中需要统一的异常处理机制，提升协议栈的健壮性和可靠性
            sendError(ctx, BAD_REQUEST);
        }
        //通过HttXmlRequest和反序列化后的Order对象，构造HttpXmlRequest实例
        HttpXmlRequest request = new HttpXmlRequest(msg, decode0(ctx, msg.content()));
        //将request添加到解码结果List列表中
        out.add(request);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure：" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
