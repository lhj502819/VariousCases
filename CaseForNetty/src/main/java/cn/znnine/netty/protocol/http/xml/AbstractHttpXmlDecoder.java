package cn.znnine.netty.protocol.http.xml;

import cn.znnine.netty.protocol.http.xml.pojo.Customer;
import cn.znnine.netty.protocol.http.xml.pojo.Order;
import cn.znnine.netty.protocol.http.xml.pojo.Shipping;
import com.thoughtworks.xstream.XStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.charset.Charset;

/**
 * Description：请求消息解码抽象基础类
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/11/12
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {
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
        String content = body.toString(UTF_8);
        if(isPrint){
            System.out.println("The body is : " + content);
        }
        XStream xs = new XStream();
        xs.setMode(XStream.NO_REFERENCES);
        //注册使用了注解的VO
        xs.processAnnotations(new Class[]{Order.class, Customer.class, Shipping.class});
        Object result = xs.fromXML(content);
        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("decode fail");
    }
}
