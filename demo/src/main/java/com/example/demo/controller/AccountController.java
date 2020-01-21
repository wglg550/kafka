package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @GetMapping("/transfer")
    public String test() {
        try {
            // andy 给lucy转账50元
            accountService.transfer(1, 2, 50);
            return "转账成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "转账失败";
        }
    }

    @PostMapping("/insert")
    public void insert(@RequestBody Account account) {
        accountService.save(account);
    }

    /**
     * kafka事务，基于KafkaTransactionManager
     */
    @GetMapping("/kafkaTrans")
    @Transactional(rollbackFor = Exception.class)
    public void kafkaTrans() {
        kafkaTemplate.send("topic.quick.tran", "test transactional annotation5");
//        int i = 1 / 0;
//        throw new RuntimeException("fail");
    }

    /**
     * kafka事务，本地事务，不需要开启KafkaTransactionManager
     */
    @GetMapping("/kafkaLocalTrans")
    @Transactional(rollbackFor = Exception.class)
    public void kafkaLocalTrans() {
        //这种方式开启事务是不需要配置事务管理器的，也可以称为本地事务
        kafkaTemplate.executeInTransaction(new KafkaOperations.OperationsCallback() {
            @Override
            public Object doInOperations(KafkaOperations kafkaOperations) {
                kafkaOperations.send("topic.quick.tran", "test executeInTransaction3");
                throw new RuntimeException("fail");
//                return true;
            }
        });
    }
}