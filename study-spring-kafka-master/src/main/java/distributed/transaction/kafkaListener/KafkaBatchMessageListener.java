package distributed.transaction.kafkaListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchMessageListener;

import java.util.List;

/**
 * 自动提交offset
 *
 * @Description: //一次可以处理一批消息,每一批次的消息总条数是随机的,但可以在消费者的配置中设置一个最大值（max.poll.records,比如设置了最大拉取的消息条数为100,那么onMessage方法每次接受到的消息条数是随机的,但最大不会超过100）
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/21
 */
public class KafkaBatchMessageListener<K, V> implements BatchMessageListener<K, V> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaBatchMessageListener.class);

    @Override
    public void onMessage(List<ConsumerRecord<K, V>> data) {
        LOGGER.debug("data={}" + data);
    }
}
