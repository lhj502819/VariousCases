package cn.znnine.netty.protocol.priv.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;

/**
 * 消息解码工具类
 *
 * 消息的编解码类按照
 *
 * @author lihongjian
 * @since 2021/11/22
 */
public class MarshallingDecoder {
    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws Exception {
        this.unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    public Object decode(ByteBuf in) throws Exception{
        int objectSize = in.readInt();
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
        try {
            unmarshaller.start(input);
            Object object = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return object;
        }finally {
            unmarshaller.close();
        }
    }


}
