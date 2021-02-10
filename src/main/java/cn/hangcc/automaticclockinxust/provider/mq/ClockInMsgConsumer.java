/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.provider.mq;

import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.AliSmsBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.TaskBiz;
import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.dao.AutomaticClockIn.UserLogsDao;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
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

    @Reference
    private UserLogsDao userLogsDao;

    @Resource
    private AliSmsBiz aliSmsBiz;

    @KafkaListener(topics = AutomaticClockInConstants.KAFKA_PUNCH_INFO_TOPIC)
    public void listen(ConsumerRecord<?, ?> record) {
        // kafka接受到的签到信息
        ClockInMsgModel msg = (ClockInMsgModel) record.value();
        try {
            for (int i = 0; i < AutomaticClockInConstants.CLOCK_IN_FAILED_RETRY_COUNT; i++) {
                if (taskBiz.executeTask(msg)) {
                    userLogsDao.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_SUCCESS_CONTENT);
                    return ;
                }
            }
            userLogsDao.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_FAILED_CONTENT);
            aliSmsBiz.sendClockInFailedMsg(msg);
            userLogsDao.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_FAILED_SEND_SMS_CONTENT);
        } catch (Exception e) {
            log.error("ClockInMsgConsumer.listen | 消费消息时出现异常: msg:{}, e = ", msg, e);
            userLogsDao.insert(msg.getSchoolId(), msg.getName(), AutomaticClockInConstants.CLOCK_IN_FAILED_EXCEPTION);
        }
    }
}