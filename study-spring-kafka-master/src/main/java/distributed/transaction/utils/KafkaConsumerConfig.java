package distributed.transaction.utils;

import distributed.transaction.kafkaListener.KafkaMessageListener;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: kafka消费者 单个读取数据
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2018/12/3
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

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

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(ProducerFactory producerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
//        factory.getContainerProperties().setPollTimeout(1500);
//        factory.getContainerProperties().setTransactionManager(new KafkaTransactionManager<>(producerFactory));
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return propsMap;
    }

//    /**
//     * @return 监听方式之一
//     * @Bean 用来kafka注册监听
//     */
//    @Bean
//    public Listener listener() {
//        return new Listener();
//    }

    @Bean
    public KafkaMessageListener listener() {
        return new KafkaMessageListener();
    }

    /**
     * @return 监听方式之一
     * @Bean 用来kafka注册监听
     */
//    @Bean
//    public KafkaMessageListenerContainer demoListenerContainer() {
//        ContainerProperties properties = new ContainerProperties("wangliang_test1");
//        properties.setGroupId("bean");
//        properties.setMessageListener(new MessageListener<Integer, String>() {
//            private Logger log = LoggerFactory.getLogger(this.getClass());
//
//            @Override
//            public void onMessage(ConsumerRecord<Integer, String> record) {
//                log.info("wangliang_test1 receive : " + record.toString());
//            }
//        });
//        return new KafkaMessageListenerContainer(consumerFactory(), properties);
//    }
}