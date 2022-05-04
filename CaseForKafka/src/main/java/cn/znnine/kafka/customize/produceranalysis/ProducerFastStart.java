package cn.znnine.kafka.customize.produceranalysis;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * 生产者客户端示例
 *
 * @author lihongjian
 * @since 2022/4/23
 */
public class ProducerFastStart {

    public static final String brokerList = "192.168.238.128:9092,192.168.238.130:9092,192.168.238.131:9092";
    public static final String topic = "test";

    public static Properties initConfig(){
        Properties properties = new Properties();
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG , "producer.client.id.demo");
        return properties;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();
        //配置生产者客户端参数并创建KafkaProducer实例
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //构建发送的消息
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello2", "worldee2");
        //发送消息
        try {
            /**
             * 发送消息的三种模式：发后即忘（fire-and-forget）、同步（sync）、异步（async）
             *
             * 以下这种为 发后即忘（fire-and-forget）模式
             */
            //同步
//            RecordMetadata recordMetadata = producer.send(record).get();
//            System.out.println("recordMetadata: " + recordMetadata);
            //异步
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println("发送成功：" + metadata.partition() + "-" + metadata.offset());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关闭生产者客户端示例
        producer.close();
    }
}
