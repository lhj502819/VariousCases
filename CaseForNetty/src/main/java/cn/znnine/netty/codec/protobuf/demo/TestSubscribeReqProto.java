package cn.znnine.netty.codec.protobuf.demo;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * Protobuf的使用
 *
 * @author lihongjian
 * @since 2021/10/23
 */
public class TestSubscribeReqProto {
    /**
     * 编码
     */
    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        //编码时通过调用实例的toByteArray即可将SubscribeReq编码为byte数组
        return req.toByteArray();
    }

    /**
     * 解码
     */
    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        //解码时通过SubscribeReqProto.SubscribeReq的静态方法parseFrom将二进制byte数组解码为原始的对象
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("零壹玖");
        builder.setProductName("Netty Book");
        List<String> addresss = new ArrayList<>();
        addresss.add("nanjing");
        addresss.add("bejing");
        //通过AddAll添加集合
        builder.addAllAddress(addresss);
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode:" + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After decode : " +req.toString());
        System.out.println("Assert equal : --->" + req2.equals(req));
    }
}
