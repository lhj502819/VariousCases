package cn.znnine.kafka.consumeranalysis;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 客户端代码示例
 *
 * @author lihongjian
 * @since 2022/4/23
 */
public class ConsumerAnalysis {

    public static final String brokerList = "192.168.238.128:9092,192.168.238.130:9092,192.168.238.131:9092";
    public static final String topic = "test";
    public static final String groupId = "test-group-x";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static Properties initConfig(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //设置消费组的名称
        properties.put("group.id", groupId);
        properties.put("client.id", "consumer.client.id.demo");
        return properties;
    }

    public static void main(String[] args) {


        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(initConfig());

        // 订阅topic，可以订阅多个topic，也可以通过正则表达式订阅多个topic
//        consumer.subscribe(Collections.singletonList(topic));

        // 如果事先不知道topic有多少个分区，可以通过partitionsFor获取topic的分区元数据信息
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        List<TopicPartition> partitions = Lists.newArrayList();
        //遍历所有的分区
        for (PartitionInfo partitionInfo : partitionInfos) {
            partitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
        }
//        TopicPartition topicPartition = new TopicPartition(topic, 0);
        //通过assign只订阅指定topic的指定分区
        consumer.assign(partitions);

//        //取消订阅，如果将#subscribe或者#assign的分区设置为空集合，等价于#unsubscribe
//        consumer.unsubscribe();
        //如果没有订阅任何topic，再继续执行消费就会报IllegalStateException异常

        // 不断循环消费消息
        while (isRunning.get()){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
//            for (ConsumerRecord<String, String> record : records) {
//                String message = String.format("消费到消息：%s, partition: %d, offset: %d", record.value(), record.partition(), record.offset());
//                System.out.println(message);
//            }
            for (TopicPartition partition : records.partitions()) {
                System.out.println("partition: " + partition.partition());
            }
        }

    }

}
