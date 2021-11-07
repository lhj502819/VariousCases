package cn.znnine.netty.protocol.http.xml;

import cn.znnine.netty.protocol.http.xml.pojo.Address;
import cn.znnine.netty.protocol.http.xml.pojo.Order;
import com.thoughtworks.xstream.XStream;

/**
 * TODO
 *
 * @author lihongjian
 * @since 2021/11/7
 */
public class TestXStream {
    public static void main(String[] args) {
        toEntity(toXml());
    }

    public static void toEntity(String xml) {
        XStream xs = new XStream();
        xs.setMode(XStream.NO_REFERENCES);
        xs.processAnnotations(new Class[]{Order.class, Address.class});
        Order order = (Order) xs.fromXML(xml);
        System.out.println(order);
    }

    public static String toXml() {
        XStream xs = new XStream();
        Address address = new Address();
        address.setState("易县");
        address.setStreet1("中国");
        address.setStreet2("河北省");
        Order order = new Order();
        order.setBillTo(address);
        xs.setMode(XStream.NO_REFERENCES);
        xs.processAnnotations(new Class[]{Address.class});
        String xml = xs.toXML(order);
        System.out.println(xml);
        return xml;
    }

}
