package cn.znnine.kafka.customizeserializer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;

/**
 * Description：自定义反序列化器
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/4/26
 */
public class CompanyDeserializer implements Deserializer<Company> {

    @Override
    public Company deserialize(String topic, byte[] data) {
        if(data == null){
            return null;
        }
        if(data.length < 8){
            throw new SerializationException("数据长度不足");
        }
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int nameLen , addressLen;
        String name = null,address = null;

        nameLen = buffer.getInt();
        byte[] nameBytes = new byte[nameLen];
        buffer.get(nameBytes);
        addressLen = buffer.getInt();
        byte[] addressBytes = new byte[addressLen];
        buffer.get(addressBytes);

        try {
            name = new String(nameBytes,"UTF-8");
            address = new String(addressBytes,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Company(name,address);
    }
}
