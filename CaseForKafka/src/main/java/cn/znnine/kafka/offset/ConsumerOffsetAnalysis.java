package cn.znnine.kafka.offset;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;

/**
 * 客户端代码示例
 *
 * @author lihongjian
 * @since 2022/4/23
 */
public class ConsumerOffsetAnalysis {

    public static final String brokerList = "192.168.238.128:9092,192.168.238.130:9092,192.168.238.131:9092";
    public static final String topic = "test";
    public static final String groupId = "test-group-x-kk";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //设置消费组的名称
        properties.put(GROUP_ID_CONFIG, groupId);
        properties.put(CLIENT_ID_CONFIG, "consumer.client.id.demoX");
        return properties;
    }

    public static void main(String[] args) {


        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(initConfig());

        TopicPartition tp = new TopicPartition(topic, 2);
        consumer.assign(Lists.newArrayList(tp));
        long lastConsumedOffset = -1;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            if (records.isEmpty()) {
                break;
            }
            List<ConsumerRecord<String, String>> partitionRecords = records.records(tp);
            lastConsumedOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
//            consumer.commitSync(Collections.singletonMap(tp, new OffsetAndMetadata(lastConsumedOffset)));
            /**
             * 异步提交，带回调函数
             */
//            consumer.commitAsync((offsets, exception) -> {
//                if (exception != null) {
//                    System.out.println("commit failed, exception: " + exception.getMessage());
//                } else {
//                    System.out.println(offsets);
//                }
//            });
            consumer.commitSync();//同步提交消费位移
        }

        System.out.println("consumed offset is " + lastConsumedOffset);
        Map<TopicPartition, OffsetAndMetadata> committed = consumer.committed(Sets.newHashSet(tp));
        OffsetAndMetadata offsetAndMetadata = committed.get(tp);
        System.out.println("committed offset is " + offsetAndMetadata.offset());
        consumer.position(tp);
        System.out.println("the offset of the next record is  " + consumer.position(tp));

    }

}
