package distributed.transaction.kafkaListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

/**
 * 手动提交offset
 *
 * @Description: 一次处理一批消息, 处理完这一批消息之后，在批量提交offset,需要在消费者的配置中设置<property name="ackMode" value="MANUAL"/>
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/21
 */
public class KafkaBatchAcknowledgingMessageListener<K, V> implements BatchAcknowledgingMessageListener<K, V> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaBatchAcknowledgingMessageListener.class);

    @Override
    @KafkaListener(topics = {"wangliang_test1"})
    public void onMessage(List<ConsumerRecord<K, V>> data, Acknowledgment acknowledgment) {
        LOGGER.debug("data={}" + data + "acknowledgment={}" + acknowledgment);
    }
}
