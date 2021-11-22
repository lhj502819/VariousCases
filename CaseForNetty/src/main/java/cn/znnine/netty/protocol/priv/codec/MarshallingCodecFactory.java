package cn.znnine.netty.protocol.priv.codec;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.jboss.marshalling.*;

/**
 * 创建MarshallingDecoder和Encoder工厂
 *
 * @author lihongjian
 * @since 2021/10/24
 */
public class MarshallingCodecFactory {
    /**
     * 创建JBoss Marshalling解码器MarshallingDecoder
     */
    public static Marshaller buildMarshalling() throws Exception{
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(configuration);
        return marshaller;
    }

    /**
     * 创建JBoss Unmarshaller
     */
    public static Unmarshaller buildUnMarshalling() throws Exception{
        final MarshallerFactory marshallerFactory =
                Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        final Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(configuration);
        return unmarshaller;
    }

}
