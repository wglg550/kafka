package distributed.transaction.kafkaListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;

/**
 * 自动提交offset
 *
 * @Description: //一次处理一条消息,消费者如果实现该接口的话,如果配置中设置max.poll.records参数大于1的话是无效的,因为它一次只处理一条消息
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/21
 */
public class KafkaMessageListener<K, V> implements MessageListener<K, V> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaMessageListener.class);

    @Override
    @KafkaListener(topics = {"wangliang_test1"})
    public void onMessage(ConsumerRecord<K, V> data) {
        LOGGER.debug("data={}" + data);
    }
}
