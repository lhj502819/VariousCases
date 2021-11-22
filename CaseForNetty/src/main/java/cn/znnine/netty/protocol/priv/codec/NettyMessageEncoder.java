package cn.znnine.netty.protocol.priv.codec;

import cn.znnine.netty.protocol.priv.struct.Header;
import cn.znnine.netty.protocol.priv.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * P：需要分别定义NettyMessageDecoder和NettyMessageEncoder用于NettyMessage消息的编解码
 *
 *  消息编码类
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    /**
     * 使用JBoss的Marshalling去编解码
     */
    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws Exception{
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if(msg == null || msg.getHeader() == null){
            throw new Exception("The encode message is null");
        }
        ByteBuf sendBuf = Unpooled.buffer();
        Header header = msg.getHeader();
        sendBuf.writeInt(header.getCrcCode());
        sendBuf.writeInt(header.getLength());
        sendBuf.writeLong(header.getSessionId());
        sendBuf.writeByte(header.getType());
        sendBuf.writeByte(header.getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes(StandardCharsets.UTF_8);
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value,sendBuf);
        }

        key = null;
        keyArray = null;
        value = null;
        if(msg.getBody()!= null){
            marshallingEncoder.encode(msg.getBody(),sendBuf);
        }else {
            sendBuf.writeInt(0);
            sendBuf.setInt(4,sendBuf.readableBytes());
        }
    }
}
