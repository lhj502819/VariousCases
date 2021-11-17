package cn.znnine.netty.protocol.http.xml;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * Description：请求消息解码抽象基础类
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/11/12
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

    private IBindingFactory factory;

    private StringReader reader;

    private Class<?> clazz;
    //是否输出码流的标志
    private boolean isPrint;

    private static final String CHARSET_NAME = "UTF-8";

    private static final Charset UTF_8 = Charset.forName(CHARSET_NAME);
    //默认设置为不输出码流
    protected AbstractHttpXmlDecoder(Class<?> clazz){
        this(clazz,false);
    }

    protected AbstractHttpXmlDecoder(Class<?> clazz, boolean isPrint) {
        this.clazz = clazz;
        this.isPrint = isPrint;
    }

    protected Object decode0(ChannelHandlerContext arg0 , ByteBuf body) throws Exception{
        //通过JiBx类库将XML转换成POJO对象
        factory = BindingDirectory.getFactory(clazz);
        String content = body.toString(UTF_8);
        if(isPrint){
            System.out.println("The body is : " + content);
        }
        reader = new StringReader(content);
        IUnmarshallingContext uctx = factory.createUnmarshallingContext();
        Object result = uctx.unmarshalDocument(reader);
        reader.close();;
        reader = null;
        return result;
    }

    @Skip
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(reader != null){
            reader.close();
            reader = null;
        }
    }
}
