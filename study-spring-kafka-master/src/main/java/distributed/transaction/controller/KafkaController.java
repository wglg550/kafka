package distributed.transaction.controller;

import distributed.transaction.model.User;
import distributed.transaction.service.UserService;
import distributed.transaction.utils.KafkaSendResultHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("kafkaTest/kafka")
public class KafkaController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private KafkaSendResultHandler producerListener;
    @Autowired
    private UserService userService;

    @GetMapping("/send/{message}")
    public void sendKafka(@PathVariable String message) {
        try {
            LOGGER.debug("kafka的消息={}", message);
            //默认异步发送消息
//            kafkaTemplate.send("wangliang_test1", "hahahah1", message);
//            kafkaTemplate.sendDefault("1213","234");
            //消息监听回调
//            kafkaTemplate.setProducerListener(producerListener);
            kafkaTemplate.send("wangliang_test1", "test producer listen", message);
            //同步发送消息
//            kafkaTemplate.send("wangliang_test1", "test sync send message", message).get();
            LOGGER.debug("发送kafka成功.");
        } catch (Exception e) {
            LOGGER.error("发送kafka失败", e);
        }
    }

    @GetMapping("/addUser")
    public void addUser(@RequestParam Long name) {
        User user = new User("wangliang_test" + name);
        userService.addUser(user);
    }
}
