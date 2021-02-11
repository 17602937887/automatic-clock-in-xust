/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.provider.conterller.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.AliSmsBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.ParamCheckBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.TaskBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.UserInfoBiz;
import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.common.response.ApiResponse;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import cn.hangcc.automaticclockinxust.service.AutomaticPunch.UserInfoService;
import cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch.UserInfoModelConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

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
    private AliSmsBiz aliSmsBiz;

    @Resource
    private TaskBiz taskBiz;

    @Resource
    private KafkaTemplate kafkaTemplate;

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
            kafkaTemplate.send(AutomaticClockInConstants.KAFKA_SEND_SMS_TOPIC, userInfoModel);
            // 直接进行一次打卡
            taskBiz.executeTask(UserInfoModelConverter.convertToClockInMsgModel(userInfoModel));
            return ApiResponse.buildSuccess();
        } catch (Exception e) {
            log.error("UserInfoController.addUser | 用户添加任务时出现异常, url:{}, e=", url, e);
            return ApiResponse.buildFailure();
        }
    }

    private void setUserAttribute(UserInfoModel user, String url, Long schoolId, String email, Integer status) throws IOException {
        user.setUserUrl(url);
        user.setSchoolId(schoolId);
        user.setEmail(email);
        user.setStatus(status);
    }
}