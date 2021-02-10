/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.task.AutomaticPunch;

import cn.hangcc.automaticpunchxust.common.constant.AutomaticPunchConstants;
import cn.hangcc.automaticpunchxust.common.utils.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 用户签到信息生产者 定时任务去生产消息
 *
 * @author chenhang
 * @created 2021/2/8
 */
@Slf4j
@Component
@EnableScheduling
public class ClockInMsgProducer {

    @Resource
    private KafkaTemplate kafkaTemplate;

    @Scheduled(cron = "0 * * * * ?")
    public void send() {
        String msg = "msg = " + LocalDateTime.now().toString();
        ListenableFuture future= kafkaTemplate.send(AutomaticPunchConstants.KAFKA_PUNCH_INFO_TOPIC, msg);
        String time = LocalDateUtils.getNowTime();
        future.addCallback(o -> log.info("kafka签到消息发送成功,time:{}, msg:{}", time, msg),
                throwable -> log.error("kafka签到消息发送失败,time:{}, msg:{} ", time, msg));
    }
}