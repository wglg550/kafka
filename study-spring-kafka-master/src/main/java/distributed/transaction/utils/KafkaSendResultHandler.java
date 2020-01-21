package distributed.transaction.utils;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * @Description: kafka消息回调
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2018/12/3
 */
@Component
public class KafkaSendResultHandler implements ProducerListener {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaSendResultHandler.class);


    @Override
    public void onSuccess(String s, Integer integer, Object o, Object o2, RecordMetadata recordMetadata) {
        LOGGER.debug("Message send success : " + s);
    }

    @Override
    public void onError(String s, Integer integer, Object o, Object o2, Exception e) {
        LOGGER.debug("Message send error : " + s);
    }

    @Override
    public boolean isInterestedInSuccess() {
        LOGGER.debug("kafka监听器启动");
        return true;
    }
}