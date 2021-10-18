package cn.znnine.netty.msgpack;


import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试MessagePack
 * @author lihongjian
 * @since 2021/10/17
 */
public class MsgPackTest {
    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<>();
        list.add("msgpack1");
        list.add("msgpack2");
        list.add("msgpack3");
        list.add("msgpack4");
        list.add("msgpack5");
        MessagePack msgPack = new MessagePack();
        byte[] raw = msgPack.write(list);
        List<String> dst1 = msgPack.read(raw , Templates.tList(Templates.TString));
        System.out.println(dst1.get(0));
        System.out.println(dst1.get(1));
        System.out.println(dst1.get(2));
        System.out.println(dst1.get(3));
        System.out.println(dst1.get(4));

    }
}
