package cn.znnine.netty.protocol.http.xml;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * 应答消息解码器
 *
 * @author lihongjian
 * @since 2021/11/17
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {

    public HttpXmlResponseDecoder(Class<?> clazz) {
        this(clazz,false);
    }

    public HttpXmlResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz,isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
        //通过DefaultFullHttpResponse和HTTP应答消息反序列化后的POJO对象构造HttpXmlResponse，并将其添加到解码结果列表中
        HttpXmlResponse resHttpXmlResponse = new HttpXmlResponse(msg, decode0(ctx, msg.content()));
        out.add(resHttpXmlResponse);
    }
}
