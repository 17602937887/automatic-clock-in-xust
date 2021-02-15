/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.provider.mq;

import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.AliSmsBiz;
import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送短信消费者
 *
 * @author chenhang
 * @created 2021/2/10
 */
@Slf4j
@Component
public class SmsMsgConsumer {

    @Resource
    private AliSmsBiz aliSmsBiz;

    @KafkaListener(topics = AutomaticClockInConstants.KAFKA_SEND_REGISTER_SUCCESS_SMS_TOPIC)
    public void listenRegisterSuccess(ConsumerRecord<?, ?> record) {
        try {
            UserInfoModel value = JSON.parseObject(record.value().toString(), UserInfoModel.class);
            // 发送注册成功的短信消息
            aliSmsBiz.sendRegisterSuccessMsg(value);
        } catch (Exception e) {
            log.error("调用Aliyun短信接口出现异常: e = ", e);
        }
    }

    @KafkaListener(topics = AutomaticClockInConstants.KAFKA_SEND_CLOCK_IN_FAILED_SMS_TOPIC)
    public void listenClockInFailed(ConsumerRecord<?, ?> record) {
        try {
            ClockInMsgModel value = JSON.parseObject(record.value().toString(), ClockInMsgModel.class);
            // 发送打卡失败的短信消息
            aliSmsBiz.sendClockInFailedMsg(value);
        } catch (Exception e) {
            log.error("调用Aliyun短信接口出现异常: e = ", e);
        }
    }
}