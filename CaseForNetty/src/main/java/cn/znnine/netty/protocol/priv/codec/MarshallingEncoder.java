package cn.znnine.netty.protocol.priv.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

/**
 * 消息编码工具类
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public class MarshallingEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    Marshaller marshaller;

    public MarshallingEncoder() throws Exception{
        this.marshaller = MarshallingCodecFactory.buildMarshalling();
    }

    public void encode(Object msg, ByteBuf out) throws Exception{
        try {
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();
            out.setInt(lengthPos , out.writerIndex() - lengthPos - 4);
        } finally {
            marshaller.close();
        }


    }
}
