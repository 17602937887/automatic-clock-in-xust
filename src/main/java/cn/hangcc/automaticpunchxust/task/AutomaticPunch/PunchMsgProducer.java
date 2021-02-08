/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.task.AutomaticPunch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/8
 */
@Slf4j
@Component
@EnableScheduling
public class PunchMsgProducer {

    @Resource
    private KafkaTemplate kafkaTemplate;

    @Scheduled(cron = "* * * * * ?")
    public void send() {
        String msg = LocalDateTime.now().toString();
        ListenableFuture future= kafkaTemplate.send("test", msg);
        future.addCallback(o -> System.out.printf("消息发送成功"), throwable -> System.out.printf("消息发送失败"));
    }
}