/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.task.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.TaskBiz;
import cn.hangcc.automaticclockinxust.biz.AutomaticClockIn.UserInfoBiz;
import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.List;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/15
 */
@Slf4j
@Component
@EnableScheduling
public class UserInfoUpdateTask {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserInfoBiz userInfoBiz;

    @Scheduled(cron = "0 0 12 1/3 * ? ")
    public void updateUserCookie() {
        try {
            List<UserInfoModel> userInfoModels = userInfoService.listAllUser();
            if (null != userInfoModels) {
                for (UserInfoModel model : userInfoModels) {
                    model.setCookie(userInfoBiz.getUserCookie(model.getUserUrl()));
                    userInfoService.update(model);
                }
            }
        } catch (Exception e) {
            log.error("UserInfoUpdateTask.updateUserCookie | 更新用户Cookie时出现异常, e = ", e);
        }
    }
}