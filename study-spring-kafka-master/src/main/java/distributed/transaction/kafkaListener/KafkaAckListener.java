package distributed.transaction.kafkaListener;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: kafka 使用Ack机制确认消费
 * acks=0： 设置为0表示producer不需要等待任何确认收到的信息。副本将立即加到socket buffer并认为已经发送。没有任何保障可以保证此种情况下server已经成功接收数据，同时重试配置不会发生作用
 * acks=1： 这意味着至少要等待leader已经成功将数据写入本地log，但是并没有等待所有follower是否成功写入。这种情况下，如果follower没有成功备份数据，而此时leader又挂掉，则消息会丢失。
 * acks=-1/all： 这意味着leader需要等待所有备份都成功写入日志，这种策略会保证只要有一个备份存活就不会丢失数据。这是最强的保证。
 * <p>
 * 1.生产者数据的不丢失
 * kafka的ack机制：在kafka发送数据的时候，每次发送消息都会有一个确认反馈机制，确保消息正常的能够被收到。
 * 如果是同步模式：ack机制能够保证数据的不丢失，如果ack设置为0，风险很大，一般不建议设置为0
 * producer.type=sync
 * request.required.acks=1
 * <p>
 * 如果是异步模式：通过buffer来进行控制数据的发送，有两个值来进行控制，时间阈值与消息的数量阈值，如果buffer满了数据还没有发送出去，如果设置的是立即清理模式，风险很大，一定要设置为阻塞模式
 * 结论：producer有丢数据的可能，但是可以通过配置保证消息的不丢失
 * producer.type=async
 * request.required.acks=1
 * queue.buffering.max.ms=5000
 * queue.buffering.max.messages=10000
 * queue.enqueue.timeout.ms = -1
 * batch.num.messages=200
 * <p>
 * 可以通过设置配置选项让kafka生产者不进行阻塞
 * max.block.ms 控制block的时长,当buffer空间不够或者metadata丢失时产生阻塞（block） 默认时长6000毫秒
 * props.put(“max.block.ms”, “0”);
 * <p>
 * 2.消费者数据的不丢失
 * 通过offset commit 来保证数据的不丢失，kafka自己记录了每次消费的offset数值，下次继续消费的时候，接着上次的offset进行消费即可。
 * <p>
 * Kafka的Ack机制相对于RabbitMQ的Ack机制差别比较大，刚入门Kafka的时候我也被搞蒙了，不过能弄清楚Kafka是怎么消费消息的就能理解Kafka的Ack机制了
 * 我先说说RabbitMQ的Ack机制，RabbitMQ的消费可以说是一次性的，也就是你确认消费后就立刻从硬盘或内存中删除，而且RabbitMQ粗糙点来说是顺序消费，像排队一样，一个个顺序消费，未被确认的消息则会重新回到队列中，等待监听器再次消费。
 * 但Kafka不同，Kafka是通过最新保存偏移量进行消息消费的，而且确认消费的消息并不会立刻删除，所以我们可以重复的消费未被删除的数据，当第一条消息未被确认，而第二条消息被确认的时候，Kafka会保存第二条消息的偏移量，也就是说第一条消息再也不会被监听器所获取，除非是根据第一条消息的偏移量手动获取。
 * <p>
 * 使用Kafka的Ack机制比较简单，只需简单的三步即可：
 * 设置ENABLE_AUTO_COMMIT_CONFIG=false，禁止自动提交
 * 设置AckMode=MANUAL_IMMEDIATE
 * 监听方法加入Acknowledgment ack 参数
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/1/17
 */
@Component
public class KafkaAckListener {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaAckListener.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean("ackContainerFactory")
    public ConcurrentKafkaListenerContainerFactory ackContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
//        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));
        return factory;
    }


    @KafkaListener(id = "ack", topics = "topic.quick.ack", containerFactory = "ackContainerFactory")
    public void ackListener(ConsumerRecord record, Acknowledgment ack) {
        LOGGER.info("topic.quick.ack receive : " + record.value());
        //怎么拒绝消息呢，只要在监听方法中不调用ack.acknowledge()即可
//        ack.acknowledge();

        //如果偏移量为偶数则确认消费，否则拒绝消费
        if (record.offset() % 2 == 0) {
            LOGGER.info(record.offset() + "--ack");
            ack.acknowledge();
        } else {
            LOGGER.info(record.offset() + "--nack");
            kafkaTemplate.send("topic.quick.ack", record.value());
        }
    }
}
