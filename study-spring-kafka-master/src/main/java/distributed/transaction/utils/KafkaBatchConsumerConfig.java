package distributed.transaction.utils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: kafka消费者 并发读取数据
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2018/12/3
 */
@Configuration
@EnableKafka
public class KafkaBatchConsumerConfig {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaBatchConsumerConfig.class);

    @Value("${kafka.consumer.servers}")
    private String servers;
    @Value("${kafka.consumer.enable.auto.commit}")
    private boolean enableAutoCommit;
    @Value("${kafka.consumer.session.timeout}")
    private String sessionTimeout;
    @Value("${kafka.consumer.auto.commit.interval}")
    private String autoCommitInterval;
    @Value("${kafka.consumer.group.id}")
    private String groupId;
    @Value("${kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${kafka.consumer.concurrency}")
    private int concurrency;

    @Bean("batchContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        //设置并发量，小于或等于Topic的分区数
        //注意：设置的并发量不能大于partition的数量，如果需要提高吞吐量，可以通过增加partition的数量达到快速提升吞吐量的效果。
        factory.setConcurrency(5);
        //设置为批量监听
        factory.setBatchListener(true);
        //需要ack手动提交offset则设置
//        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
//        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
//        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
//        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
//        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //需要ack手动提交offset则设置为false
//        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        //一次拉取消息数量
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "5");
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return propsMap;
    }

//    /**
//     * 创建topic和partition
//     *
//     * @return
//     */
//    @Bean
//    public NewTopic batchTopic() {
//        return new NewTopic("topic.quick.batch", 8, (short) 1);
//    }
//
//    /**
//     * 批量监听(并发)
//     *
//     * @param data
//     */
//    @KafkaListener(id = "batch", clientIdPrefix = "batch", topics = {"topic.quick.batch"}, containerFactory = "batchContainerFactory")
//    public void batchListener(List<String> data) {
//        LOGGER.info("topic.quick.batch  receive : ");
//        for (String s : data) {
//            LOGGER.info(s);
//        }
//    }

//    /**
//     * 创建topic和partition
//     *
//     * @return
//     */
//    @Bean
//    public NewTopic batchTopic() {
//        return new NewTopic("topic.quick.ack.batch", 8, (short) 1);
//    }
//
//    /**
//     * 批量监听(并发)
//     *
//     * @param data
//     */
//    @KafkaListener(id = "ack.batch", clientIdPrefix = "ack.batch", topics = {"topic.quick.ack.batch"}, containerFactory = "batchContainerFactory")
//    public void batchListener(List<String> data, Acknowledgment ack) {
//        LOGGER.info("topic.quick.ack.batch  receive : ");
//        for (String s : data) {
//            LOGGER.info(s);
//            ack.acknowledge();
//        }
//    }

//    /**
//     * 创建topic和partition
//     *
//     * @return
//     */
//    @Bean
//    public NewTopic batchWithPartitionTopic() {
//        return new NewTopic("topic.quick.batch.partition", 8, (short) 1);
//    }

//    /**
//     * 监听某个topic和partition，可以指定offset
//     *
//     * @param data
//     * @TopicPartition：topic--需要监听的Topic的名称，partitions --需要监听Topic的分区id，
//     * partitionOffsets --可以设置从某个偏移量开始监听
//     * @PartitionOffset：partition --分区Id，非数组，initialOffset --初始偏移量
//     */
//    @KafkaListener(id = "batchWithPartition", clientIdPrefix = "bwp", containerFactory = "batchContainerFactory",
//            topicPartitions = {
//                    @TopicPartition(topic = "topic.quick.batch.partition", partitions = {"1", "3"}),
//                    @TopicPartition(topic = "topic.quick.batch.partition", partitions = {"0", "4"},
//                            partitionOffsets = @PartitionOffset(partition = "2", initialOffset = "100"))
//            }
//    )
//    public void batchListenerWithPartition(List<String> data) {
//        LOGGER.info("topic.quick.batch.partition2  receive : ");
//        for (String s : data) {
//            LOGGER.info(s);
//        }
//    }

//    /**
//     * 第一个是参数是topic名字，第二个参数是分区个数，第三个是topic的副本数量
//     * 创建topic和partition
//     *
//     * @return
//     */
//    @Bean
//    public NewTopic batchWithPartitionTopic() {
//        return new NewTopic("topic.quick.anno", 8, (short) 1);
//    }

//    /**
//     * 注解方式获取消息头及消息体
//     *
//     * @param data
//     * @param key
//     * @param partition
//     * @param topic
//     * @param ts
//     */
//    @KafkaListener(id = "anno", topics = "topic.quick.anno")
//    public void annoListener(@Payload String data,
//                             @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
//                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
//                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
//                             @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
//        LOGGER.info("topic.quick.anno receive : \n" +
//                "data : " + data + "\n" +
//                "key : " + key + "\n" +
//                "partitionId : " + partition + "\n" +
//                "topic : " + topic + "\n" +
//                "timestamp : " + ts + "\n"
//        );
//    }

    //    /**
//     * @return 监听方式之一
//     * @Bean 用来kafka注册监听
//     */
//    @Bean
//    public Listener listener() {
//        return new Listener();
//    }
//    @Bean
//    public KafkaMessageListenerContainer listener() {
//        ContainerProperties properties = new ContainerProperties("topic.quick.batch");
//        properties.setGroupId("batch");
//        properties.setMessageListener(new KafkaBatchMessageListener());
//        return new KafkaMessageListenerContainer(consumerFactory(), properties);
//    }

//    @Bean
//    public KafkaBatchMessageListener listener() {
//        return new KafkaBatchMessageListener();
//    }
}