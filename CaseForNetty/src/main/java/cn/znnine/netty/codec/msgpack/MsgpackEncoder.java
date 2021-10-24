package cn.znnine.netty.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Msgpack编码器
 * 继承MessageToByteEncoder，它负责将Object累心的POJO对象编码为byte数组，然后写入到ByteBuf中去
 * @author lihongjian
 * @since 2021/10/18
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack msgPack = new MessagePack();
        //序列化
        byte[] raw = msgPack.write(msg);
        out.writeBytes(raw);
    }
}
