package cn.znnine.netty.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Msgpack解码器
 *
 * @author lihongjian
 * @since 2021/10/18
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    /**
     * 首先从数据报msg中获取需要解码的byte数组，然后调用MessagePack的read方法，将其反序列化为Object对象，将解码器后的对象加入到解码列表arg2中，这样就完成了MessagePack的解码操作
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final  byte[] array;
        final int length = msg.readableBytes();
        array = new byte[length];
        //从数据报msg中获取需要解码的byte数组
        msg.getBytes(msg.readerIndex(),array,0,length);
        MessagePack msgPack = new MessagePack();
        //read方法将需要解码的byte数组反序列化为Object对象
        out.add(msgPack.read(array));
    }
}
