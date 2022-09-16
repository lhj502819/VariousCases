package cn.znnine.kafka.protostuff;

import cn.znnine.kafka.customize.serializer.Company;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * ProtostuffDeserializer
 *
 * @author lihongjian
 * @since 2022/5/8
 */
public class ProtostuffDeserializer implements Deserializer<Company> {
    @Override
    public Company deserialize(String topic, byte[] data) {
        if(data == null){
            return null;
        }
        Schema<Company> schema = RuntimeSchema.getSchema(Company.class);
        Company company = new Company();
        ProtostuffIOUtil.mergeFrom(data, company, schema);
        return company;
    }
}
