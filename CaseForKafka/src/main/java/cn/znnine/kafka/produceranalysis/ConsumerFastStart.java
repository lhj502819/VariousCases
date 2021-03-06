package cn.znnine.kafka.produceranalysis;

import cn.znnine.kafka.protostuff.ProtostuffDeserializer;
import cn.znnine.kafka.customize.serializer.Company;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 客户端代码示例
 *
 * @author lihongjian
 * @since 2022/4/23
 */
public class ConsumerFastStart {

    public static final String brokerList = "192.168.238.128:9092,192.168.238.130:9092,192.168.238.131:9092";
    public static final String topic = "test";
    public static final String groupId = "test-group-x";


    public static void main(String[] args) {
        Properties properties = new Properties();
//        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", ProtostuffDeserializer.class.getName());
        properties.put("bootstrap.servers", brokerList);
        //设置消费组的名称
        properties.put("group.id", groupId);

        // 创建消费者
        KafkaConsumer<String, Company> consumer = new KafkaConsumer<>(properties);
        // 订阅主题
        consumer.subscribe(Collections.singletonList(topic));
        //循环消费消息
        while (true){
            ConsumerRecords<String, Company> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, Company> record : records) {
                System.out.println("消费到消息：" + record.value());
            }
        }
    }

}
