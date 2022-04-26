package cn.znnine.kafka.pojo;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;

/**
 * Description：自定义序列化器
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/4/26
 */
public class CompanySerializer implements Serializer<Company> {
    @Override
    public byte[] serialize(String topic, Company data) {
        if (data == null) {
            return null;
        }
        byte[] name,address;
        try {
            if (data.getName() != null) {
                name = data.getName().getBytes("UTF-8");
            }else {
                name = new byte[0];
            }
            if (data.getAddress() != null) {
                address = data.getAddress().getBytes("UTF-8");
            }else {
                address = new byte[0];
            }

            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + 4 + name.length + address.length);
            byteBuffer.putInt(name.length);
            byteBuffer.put(name);
            byteBuffer.putInt(address.length);
            byteBuffer.put(address);
            return byteBuffer.array();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new byte[0];
    }
}
