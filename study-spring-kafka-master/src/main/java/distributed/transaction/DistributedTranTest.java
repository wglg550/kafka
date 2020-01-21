package distributed.transaction;

import distributed.transaction.mappers.UserMapper;
import distributed.transaction.model.User;
import distributed.transaction.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用kafka实现分布式事务
 *
 * @author Dean
 * @see "<a href="https://www.cnblogs.com/520playboy/p/6715438.html">使用事件和消息队列实现分布式事务</a>"
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
public class DistributedTranTest {

    @Resource
    private UserService userService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private UserMapper userMapper;

//    @Test
//    public void addUser() {
//        int userCount = 10;
//        for (int i = 0; i < userCount; i++) {
//            User user = new User("foo" + i);
//            userService.addUser(user);
//        }
//    }

//    @Test
//    public void testBatch() {
//        for (int i = 0; i < 12; i++) {
//            kafkaTemplate.send("topic.quick.batch", "test batch listener,dataNum-" + i);
//        }
//    }

    @Test
    public void testAckBatch() {
        for (int i = 0; i < 12; i++) {
            kafkaTemplate.send("topic.quick.ack.batch", "test ack batch listener,dataNum-" + i);
        }
    }

//    /**
//     * 发送指定topic和partition消息
//     */
//    @Test
//    public void testBatch() {
//        Map map = new HashMap<>();
//        map.put(KafkaHeaders.TOPIC, "topic.quick.anno");
//        map.put(KafkaHeaders.MESSAGE_KEY, "222");
//        map.put(KafkaHeaders.PARTITION_ID, 2);
//        map.put(KafkaHeaders.TIMESTAMP, System.currentTimeMillis());
//        kafkaTemplate.send(new GenericMessage<>("test anno listener", map));
//    }

    /**
     * ack机制发送消息
     *
     * @throws InterruptedException
     */
//    @Test
//    public void testAck() throws InterruptedException {
//        for (int i = 0; i < 5; i++) {
//            kafkaTemplate.send("topic.quick.ack", i + "");
//        }
//    }
    @Test
    @Transactional
    public void testTransactionalAnnotation() throws InterruptedException {
//        kafkaTemplate.send("topic.quick.tran", "test transactional annotation2");
//        throw new RuntimeException("fail");

        User user = new User("wangliang_test123");
//        userMapper.save(user);
        userService.addUser(user);
//        throw new RuntimeException("fail");
//        Map<TopicPartition, OffsetAndMetadata> commits = new HashMap<>();
//        commits.put(new TopicPartition("topic.quick.tran", 0),
//                new OffsetAndMetadata(1));
//        kafkaTemplate.sendOffsetsToTransaction(commits, "topic.quick.tran");

//        ApplicationContext context = new AnnotationConfigApplicationContext(KafkaTransactionManagerTest.class);
//        KafkaTranService service = context.getBean(KafkaTranService.class);
//        service.send("topic.quick.tran", "test transactional annotation1111");

        //这种方式开启事务是不需要配置事务管理器的，也可以称为本地事务
//        kafkaTemplate.executeInTransaction(new KafkaOperations.OperationsCallback() {
//            @Override
//            public Object doInOperations(KafkaOperations kafkaOperations) {
//                kafkaOperations.send("topic.quick.tran", "test executeInTransaction3");
//                throw new RuntimeException("fail");
////                return true;
//            }
//        });
    }

//    @Test
//    public void testMessage() {
//        kafkaTemplate.send("wangliang_test1", "test producer listen", "1");
//    }
}
