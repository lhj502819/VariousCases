package cn.znnine.kafka.protostuff;

import cn.znnine.kafka.customize.serializer.Company;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.kafka.common.serialization.Serializer;

/**
 * 自定义Protostuff序列化器
 *
 * @author lihongjian
 * @since 2022/5/8
 */
public class ProtostuffSerializer implements Serializer<Company> {
    @Override
    public byte[] serialize(String topic, Company data) {
        if (data == null) {
            return null;
        }
        Schema<Company> schema = RuntimeSchema.getSchema(Company.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(data, schema, buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            buffer.clear();
        }
        return protostuff;
    }
}
