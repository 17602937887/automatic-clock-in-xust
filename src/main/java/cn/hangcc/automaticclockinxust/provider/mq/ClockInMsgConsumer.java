/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.provider.mq;

import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.TaskBiz;
import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserLogsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户签到信息消费者
 *
 * @author chenhang
 * @created 2021/2/10
 */
@Slf4j
@Component
public class ClockInMsgConsumer {

    @Resource
    private TaskBiz taskBiz;

    @Resource
    private UserLogsService userLogsService;

    @Resource
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = AutomaticClockInConstants.KAFKA_CLOCK_IN_INFO_TOPIC)
    public void listen(ConsumerRecord<?, ?> record) {
        // kafka接受到的签到信息
        ClockInMsgModel msg = (ClockInMsgModel) record.value();
        try {
            for (int i = 0; i < AutomaticClockInConstants.CLOCK_IN_FAILED_RETRY_COUNT; i++) {
                if (taskBiz.executeTask(msg)) {
                    userLogsService.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_SUCCESS_CONTENT);
                    return ;
                }
            }
            userLogsService.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_FAILED_CONTENT);
            kafkaTemplate.send(AutomaticClockInConstants.KAFKA_SEND_SMS_TOPIC, msg);
            userLogsService.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_FAILED_SEND_SMS_CONTENT);
        } catch (Exception e) {
            log.error("ClockInMsgConsumer.listen | 消费消息时出现异常: msg:{}, e = ", msg, e);
            userLogsService.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_FAILED_EXCEPTION);
        }
    }
}