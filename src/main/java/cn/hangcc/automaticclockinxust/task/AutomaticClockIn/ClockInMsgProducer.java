/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.task.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ConfigModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.ConfigService;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    @Resource
    private ConfigService configService;

    @Resource
    private UserInfoService userInfoService;

    @Scheduled(cron = "0 * * * * ?")
    public void send() {
        String msg = "msg = " + LocalDateTime.now().toString();
        ListenableFuture future= kafkaTemplate.send(AutomaticClockInConstants.KAFKA_CLOCK_IN_INFO_TOPIC, msg);
        String time = LocalDateUtils.getNowTime();
        future.addCallback(o -> log.info("kafka签到消息发送成功,time:{}, msg:{}", time, msg),
                throwable -> log.error("kafka签到消息发送失败,time:{}, msg:{} ", time, msg));
    }

    List<ClockInMsgModel> listClockInUsers() {
        try {
            // 获取当前时间
            LocalTime now = LocalTime.now();
            LocalTime morningStartTime = LocalDateUtils.getLocalTime(configService.query("morningStartTime").getValue());
            LocalTime morningEndTime = LocalDateUtils.getLocalTime(configService.query("morningEndTime").getValue());
            // 当前时刻为满足配置中心的签到时刻 取出部分满足的数据进行签到
            if (now.isAfter(morningStartTime) && now.isBefore(morningEndTime)) {
                // 查询出所有需要早上签到的用户
                List<UserInfoModel> userInfoModels = userInfoService.listMorningClockInUser();
                long diff = Duration.between(morningStartTime, morningEndTime).toMinutes();

            }
            return null;
        } catch (Exception e) {

            return null;
        }
    }
}