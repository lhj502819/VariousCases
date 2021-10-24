package cn.znnine.netty.codec.marshalling;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * 创建MarshallingDecoder和Encoder工厂
 *
 * @author lihongjian
 * @since 2021/10/24
 */
public class MarshallingCodeCFactory {
    /**
     * 创建JBoss Marshalling解码器MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder(){
        //通过Marshalling工具类获取MarshallerFactory实例，参数serial表示创建的是Java序列化工厂对象
        final MarshallerFactory marshallerFactory =
                Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        //设置版本号
        configuration.setVersion(5);
        DefaultUnmarshallerProvider provider =
                new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        //还可以设置单个消息序列化后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider);
        return decoder;
    }

    /**
     * 创建JBoss Marshalling编码器MarshallingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder(){
        MarshallerFactory marshallerFactory =
                Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        DefaultMarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }

}
