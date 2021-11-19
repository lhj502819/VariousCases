package cn.znnine.netty.protocol.http.xml;

import cn.znnine.netty.protocol.http.xml.pojo.Customer;
import cn.znnine.netty.protocol.http.xml.pojo.Order;
import cn.znnine.netty.protocol.http.xml.pojo.Shipping;
import com.thoughtworks.xstream.XStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * TODO
 *
 * @author lihongjian
 * @since 2021/11/7
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {

    final static String CHARSET_NAME = "UTF-8";

    final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) throws Exception {
        //将Order类型转换为xml流
        XStream xStream = new XStream();
        xStream.setMode(XStream.NO_REFERENCES);
        //注册使用了注解的VO
        xStream.processAnnotations(new Class[]{Order.class, Customer.class, Shipping.class});
        String xml = xStream.toXML(body);
        ByteBuf encodeBuf = Unpooled.copiedBuffer(xml, UTF_8);
        return encodeBuf;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("fail to encoder");
    }
}
