/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.provider.conterller.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.ParamCheckBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.TaskBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.UserInfoBiz;
import cn.hangcc.automaticclockinxust.common.response.ApiResponse;
import cn.hangcc.automaticclockinxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.ConfigService;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserInfoService;
import cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch.UserInfoModelConverter;
import cn.hangcc.automaticclockinxust.task.AutomaticClockIn.UserInfoUpdateTask;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants.*;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class UserInfoController {

    @Resource
    private ParamCheckBiz paramCheckBiz;

    @Resource
    private UserInfoBiz userInfoBiz;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private KafkaTemplate kafkaTemplate;

    @Resource
    private ConfigService configService;

    /**
     * 用户添加任务的请求接口
     * @param url 用户的签到打卡地址
     * @param schoolId 用户的学号
     * @param email 用户的邮箱 可选
     * @param status 用户需要打卡的状态 1.上午 2.下午 3.两次 4.用户取消当前打卡任务
     * @return 状态信息
     */
    @PostMapping("/add.json")
    public ApiResponse addUser(@RequestParam("url") String url,
                               @RequestParam("id") Long schoolId,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam("status") Integer status) {
        try {
            // 参数检查
            if (!paramCheckBiz.checkTaskUrl(url)) {
                return ApiResponse.buildFailure("Url错误,请检查!");
            }
            if (!paramCheckBiz.checkTaskSchoolId(url, schoolId)) {
                return ApiResponse.buildFailure("学号错误,请检查!");
            }
            UserInfoModel userInfoModel = userInfoBiz.getUserInfo(url, schoolId);
            // 设置相关属性值
            setUserAttribute(userInfoModel, url, schoolId, email, status);
            userInfoService.insert(userInfoModel);
            // 异步发送短信告知用户注册成功
            String smsMsg = JSON.toJSON(userInfoModel).toString();
            ListenableFuture future = kafkaTemplate.send(KAFKA_SEND_REGISTER_SUCCESS_SMS_TOPIC, smsMsg);
            String time = LocalDateUtils.getNowTime();
            future.addCallback(o -> log.info("kafka发送短信消息发送成功,time:{}, smsMsg:{}", time, smsMsg),
                    throwable -> log.error("kafka发送短信消息发送失败,time:{}, smsMsg:{}", time, smsMsg));
            // 扔到mq里面 进行一次打卡
            String clockInMsg = JSON.toJSON(UserInfoModelConverter.convertToClockInMsgModel(userInfoModel)).toString();
            future = kafkaTemplate.send(KAFKA_CLOCK_IN_INFO_TOPIC, clockInMsg);
            future.addCallback(o -> log.info("kafka发送签到消息发送成功,time:{}, clockInMsg:{}", time, clockInMsg),
                    throwable -> log.error("kafka发送签到消息发送失败,time:{}, clockInMsg:{}", time, clockInMsg));
            return ApiResponse.buildSuccess();
        } catch (Exception e) {
            log.error("UserInfoController.addUser | 用户添加任务时出现异常, url:{}, e=", url, e);
            String time = LocalDateUtils.getNowTime();
            String errorMsg = String.format("用户添加任务时出现异常:e=%s", e.getMessage().substring(0, 15));
            notifyDev(time, errorMsg);
            return ApiResponse.buildFailure();
        }
    }

    @PostMapping("/specify_execute.json")
    public ApiResponse specifyExecute(@RequestParam("schoolId") Long schoolId) {
        try {
            UserInfoModel userInfoModel = userInfoService.query(schoolId);
            // 扔到mq里面 进行一次打卡
            String time = LocalDateUtils.getNowTime();
            String clockInMsg = JSON.toJSON(UserInfoModelConverter.convertToClockInMsgModel(userInfoModel)).toString();
            ListenableFuture future = kafkaTemplate.send(KAFKA_CLOCK_IN_INFO_TOPIC, clockInMsg);
            future.addCallback(o -> log.info("kafka发送签到消息发送成功,time:{}, clockInMsg:{}", time, clockInMsg),
                    throwable -> log.error("kafka发送签到消息发送失败,time:{}, clockInMsg:{}", time, clockInMsg));
            log.info("UserInfoController.specifyExecute | 手动执行单个用户签到, schoolId:{}, userModel:{}", schoolId, userInfoModel);
            return ApiResponse.buildSuccess();
        } catch (Exception e) {
            log.error("UserInfoController.specifyExecute | 手动执行单个用户签到出现异常, schoolId:{}, e = ", schoolId, e);
            String time = LocalDateUtils.getNowTime();
            String errorMsg = String.format("手动执行单个用户签到出现异常:e=%s", e.getMessage().substring(0, 15));
            notifyDev(time, errorMsg);
            return ApiResponse.buildFailure("手动执行单个用户签到出现异常");
        }
    }

    @PostMapping("/delete.json")
    public ApiResponse delete(@RequestParam("schoolId") Long schoolId) {
        try {
            userInfoService.delete(schoolId);
            log.info("UserInfoController.delete | 删除单个用户, schoolId:{}", schoolId);
            return ApiResponse.buildSuccess();
        } catch (Exception e) {
            log.error("UserInfoController.delete | 删除单个用户出现异常, schoolId:{}, e = ", schoolId, e);
            String time = LocalDateUtils.getNowTime();
            String errorMsg = String.format("删除单个用户出现异常:e=%s", e.getMessage().substring(0, 15));
            notifyDev(time, errorMsg);
            return ApiResponse.buildFailure("删除失败");
        }
    }

    @GetMapping("/execute_all.json")
    public ApiResponse executeAllUser() {
        try {
            // 获取当前时间
            LocalTime nowTime = LocalTime.now();
            LocalTime morningStartTime = LocalDateUtils.getLocalTime(configService.query("morningStartTime").getValue());
            LocalTime morningEndTime = LocalDateUtils.getLocalTime(configService.query("morningEndTime").getValue());
            // 扔到mq里面 进行一次打卡
            String time = LocalDateUtils.getNowTime();
            // 当前时刻为满足配置中心的签到时刻 全量数据进行签到
            if (nowTime.isAfter(morningStartTime) && nowTime.isBefore(morningEndTime)) {
                List<UserInfoModel> userInfoModels = userInfoService.listMorningClockInUser();
                List<ClockInMsgModel> clockInMsgModels = userInfoModels.stream().map(UserInfoModelConverter::convertToClockInMsgModel).collect(Collectors.toList());
                clockInMsgModels.forEach(msg -> {
                    String clockInMsg = JSON.toJSON(clockInMsgModels).toString();
                    ListenableFuture future = kafkaTemplate.send(KAFKA_CLOCK_IN_INFO_TOPIC, clockInMsg);
                    future.addCallback(o -> log.info("kafka发送签到消息发送成功,time:{}, clockInMsg:{}", time, clockInMsg),
                            throwable -> log.error("kafka发送签到消息发送失败,time:{}, clockInMsg:{}", time, clockInMsg));
                    log.info("全量用户签到: userModel:{}", msg);
                });
                return ApiResponse.buildSuccess();
            }
            LocalTime eveningStartTime = LocalDateUtils.getLocalTime(configService.query("eveningStartTime").getValue());
            LocalTime eveningEndTime = LocalDateUtils.getLocalTime(configService.query("eveningEndTime").getValue());
            // 当前时刻为满足配置中心的签到时刻 取出部分满足的数据进行签到
            if (nowTime.isAfter(eveningStartTime) && nowTime.isBefore(eveningEndTime)) {
                List<UserInfoModel> userInfoModels = userInfoService.listEveningClockInUser();
                List<ClockInMsgModel> clockInMsgModels = userInfoModels.stream().map(UserInfoModelConverter::convertToClockInMsgModel).collect(Collectors.toList());
                clockInMsgModels.forEach(msg -> {
                    String clockInMsg = JSON.toJSON(clockInMsgModels).toString();
                    ListenableFuture future = kafkaTemplate.send(KAFKA_CLOCK_IN_INFO_TOPIC, clockInMsg);
                    future.addCallback(o -> log.info("kafka发送签到消息发送成功,time:{}, clockInMsg:{}", time, clockInMsg),
                            throwable -> log.error("kafka发送签到消息发送失败,time:{}, clockInMsg:{}", time, clockInMsg));
                    log.info("全量用户签到: userModel:{}", msg);
                });
                return ApiResponse.buildSuccess();
            }
            // 非配置中心进行签到的时间段 直接return
            return null;
        } catch (Exception e) {
            log.error("UserInfoController.executeAllUser | 执行全量用户签到时出现异常, e = ", e);
            String time = LocalDateUtils.getNowTime();
            String errorMsg = String.format("执行全量用户签到时出现异常:e=%s", e.getMessage().substring(0, 15));
            notifyDev(time, errorMsg);
            return ApiResponse.buildFailure();
        }
    }

    private void setUserAttribute(UserInfoModel user, String url, Long schoolId, String email, Integer status) throws IOException {
        user.setUserUrl(url);
        user.setSchoolId(schoolId);
        user.setEmail(email);
        user.setStatus(status);
    }

    private void notifyDev(String time, String errorMsg) {
        // 异步发送短信告知开发者出现异常
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        map.put("errorMsg", errorMsg);
        String msg = JSON.toJSONString(map);
        ListenableFuture future = kafkaTemplate.send(KAFKA_SEND_CLOCK_IN_EXCEPTION_SMS_TOPIC, msg);
        String nowTime = LocalDateUtils.getNowTime();
        future.addCallback(o -> log.info("kafka发送短信消息发送成功,time:{}, smsMsg:{}", nowTime, msg),
                throwable -> log.error("kafka发送短信消息发送失败,time:{}, smsMsg:{}", nowTime, msg));
    }
}