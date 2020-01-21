package distributed.transaction.kafkaListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: kafka消息监听
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2018/12/3
 */
public class Listener {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Listener.class);

//    @KafkaListener(topics = {"wangliang_test1"})
//    public void listen(ConsumerRecord<?, ?> record) {
//        LOGGER.debug("kafka的key: " + record.key());
//        LOGGER.debug("kafka的value: " + record.value().toString());
//    }

    @KafkaListener(topics = {"topic.quick.tran"})
    @Transactional
    public void listen(ConsumerRecord<?, ?> record) {
        LOGGER.debug("kafka的key: " + record.key());
        LOGGER.debug("kafka的value: " + record.value().toString());
        throw new RuntimeException("fail");
    }
}