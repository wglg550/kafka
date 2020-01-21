package distributed.transaction.kafkaListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * 手动提交offset
 *
 * @Description: 一次只处理一条消息, 并手动提交offset, 需要在消费者的配置中设置<property   name = " ackMode "   value = " MANUAL_IMMEDIATE " />
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/21
 */
public class KafkaAcknowledgingMessageListener<K, V> implements AcknowledgingMessageListener<K, V> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaAcknowledgingMessageListener.class);

    @Override
    @KafkaListener(topics = {"wangliang_test1"})
    public void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment) {
        LOGGER.debug("data={}" + data + "acknowledgment={}" + acknowledgment);
    }
}
