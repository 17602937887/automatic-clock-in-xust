/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.task.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.ConfigService;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserInfoService;
import cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch.UserInfoModelConverter;
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
import java.util.stream.Collectors;

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
        List<ClockInMsgModel> clockInMsgModels = listClockInUsers();
        if (null != clockInMsgModels) {
            for (ClockInMsgModel msg : clockInMsgModels) {
                ListenableFuture future= kafkaTemplate.send(AutomaticClockInConstants.KAFKA_CLOCK_IN_INFO_TOPIC, msg);
                String time = LocalDateUtils.getNowTime();
                future.addCallback(o -> log.info("kafka签到消息发送成功,time:{}, msg:{}", time, msg),
                        throwable -> log.error("kafka签到消息发送失败,time:{}, msg:{} ", time, msg));
            }
        }
    }

    /**
     * 获取满足条件的签到用户
     * @return 用户集合
     */
    List<ClockInMsgModel> listClockInUsers() {
        try {
            Thread.sleep(AutomaticClockInConstants.KAFKA_SLEEP_TIME);
            // 获取当前时间
            LocalTime nowTime = LocalTime.now();
            LocalTime morningStartTime = LocalDateUtils.getLocalTime(configService.query("morningStartTime").getValue());
            LocalTime morningEndTime = LocalDateUtils.getLocalTime(configService.query("morningEndTime").getValue());
            LocalDateTime executeTime = LocalDateTime.now();
            // 当前时刻为满足配置中心的签到时刻 取出部分满足的数据进行签到
            if (nowTime.isAfter(morningStartTime) && nowTime.isBefore(morningEndTime)) {
                return listMorningClockInUsers(executeTime, morningStartTime, morningEndTime);
            }
            LocalTime eveningStartTime = LocalDateUtils.getLocalTime(configService.query("eveningStartTime").getValue());
            LocalTime eveningEndTime = LocalDateUtils.getLocalTime(configService.query("eveningEndTime").getValue());
            // 当前时刻为满足配置中心的签到时刻 取出部分满足的数据进行签到
            if (nowTime.isAfter(eveningStartTime) && nowTime.isBefore(eveningEndTime)) {
                return listEveningClockInUsers(executeTime, eveningStartTime, eveningEndTime);
            }
            // 非配置中心进行签到的时间段 直接return
            return null;
        } catch (Exception e) {
            log.error("ClockInMsgProducer.listClockInUsers | 获取签到用户集合时出现异常, e = ", e);
            return null;
        }
    }

    List<ClockInMsgModel> listMorningClockInUsers(LocalDateTime executeTime, LocalTime morningStartTime, LocalTime morningEndTime) {
        // 查询出所有需要早上签到的用户
        List<UserInfoModel> userInfoModels = userInfoService.listMorningClockInUser();
        // 转为ClockInMsgModel
        List<ClockInMsgModel> clockInMsgModels = userInfoModels.stream().map(UserInfoModelConverter::convertToClockInMsgModel).collect(Collectors.toList());
        return clockInMsgModels.stream()
                .filter(model -> randomFunction(model, executeTime, morningStartTime, morningEndTime))
                .collect(Collectors.toList());
    }

    List<ClockInMsgModel> listEveningClockInUsers(LocalDateTime executeTime, LocalTime eveningStartTime, LocalTime eveningEndTime) {
        // 查询出所有需要晚上签到的用户
        List<UserInfoModel> userInfoModels = userInfoService.listEveningClockInUser();
        // 转为ClockInMsgModel
        List<ClockInMsgModel> clockInMsgModels = userInfoModels.stream().map(UserInfoModelConverter::convertToClockInMsgModel).collect(Collectors.toList());
        return clockInMsgModels.stream()
                .filter(model -> randomFunction(model, executeTime, eveningStartTime, eveningEndTime))
                .collect(Collectors.toList());
    }

    /**
     * 散列用户进行打卡的随机函数
     * @param userInfoModel 用户签到model
     * @param executeTime 定时任务开始执行的时间
     * @param startTime 配置中心开始签到的时间
     * @param endTime 配置中心结束签到的时间
     * @return 当前用户是否满足条件
     */
    private boolean randomFunction(ClockInMsgModel userInfoModel, LocalDateTime executeTime, LocalTime startTime, LocalTime endTime) {
        // yyyyMMddHHmm
        long timeVal = LocalDateUtils.getLong(executeTime);
        // 满足签到时间的分钟时长
        long diff = Duration.between(startTime, endTime).toMinutes();
        // 根据当前时间与用户学号 计算一个hash值。 因为日期始终在变化 所以可以达到用户签到时间随机的效果
        long hashVal =  (timeVal * 31 + userInfoModel.getSchoolId()) % diff;
        return hashVal == Math.abs(Duration.between(executeTime.toLocalTime(), startTime).toMinutes());
    }
}