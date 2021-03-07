/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.biz.mq;

import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.AliSmsBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.TaskBiz;
import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.common.utils.AutomaticClockInUtils;
import cn.hangcc.automaticclockinxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserLogsService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants.*;

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

    @Resource
    private AliSmsBiz aliSmsBiz;

    @KafkaListener(topics = KAFKA_CLOCK_IN_INFO_TOPIC)
    public void listen(ConsumerRecord<?, ?> record) {
        // kafka接受到的签到信息
        ClockInMsgModel msg = JSON.parseObject(record.value().toString(), ClockInMsgModel.class);
        InetAddress localHost = null;
        String ip = null;
        String hostName = null;
        String ipAndHostName = null;
        try {
            localHost = InetAddress.getLocalHost();
            if (localHost != null) {
                ip = AutomaticClockInUtils.getHostIp();
                hostName = localHost.getHostName();
                ipAndHostName = String.format(" | 签到机器信息:ip{%s},hostName:{%s}", ip, hostName);
            }
        } catch (UnknownHostException e) {
            ipAndHostName = "机器信息获取失败";
            log.info("ClockInMsgConsumer.listen | 获取机器信息失败");
        }
        try {
            log.info("接到kafka生产的签到消息ClockInMsgModel:{}", msg);
            for (int i = 0; i < CLOCK_IN_FAILED_RETRY_COUNT; i++) {
                if (taskBiz.executeTask(msg)) {
                    userLogsService.insert(msg.getSchoolId(), msg.getName(), CLOCK_IN_SUCCESS_CONTENT + ipAndHostName);
                    return ;
                }
            }
            userLogsService.insert(msg.getSchoolId(), msg.getName(), CLOCK_IN_FAILED_CONTENT + ipAndHostName);
            aliSmsBiz.sendClockInFailedMsg(msg);
            userLogsService.insert(msg.getSchoolId(), msg.getName(), CLOCK_IN_FAILED_SEND_SMS_CONTENT + ipAndHostName);
        } catch (Exception e) {
            log.error("ClockInMsgConsumer.listen | 消费消息时出现异常: msg:{}, e = ", msg, e);
            userLogsService.insert(msg.getSchoolId(), msg.getName(), CLOCK_IN_FAILED_EXCEPTION + ipAndHostName);
            aliSmsBiz.sendExceptionToDev(LocalDateUtils.getNowTime(), e.getMessage().substring(0, 30));
        }
    }
}