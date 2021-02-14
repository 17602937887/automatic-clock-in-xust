/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.biz.AutomaticClockIn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
@Slf4j
@Component
public class ParamCheckBiz {

    @Resource
    private UserInfoBiz userInfoBiz;

    /**
     * 检查用户添加任务时 post的Url是否正确
     * @param url 用户提交的url
     * @return 该url是否正确
     */
    public boolean checkTaskUrl(String url) {
        if (StringUtils.hasLength(url)) {
            try {
                return StringUtils.hasLength(userInfoBiz.getUserCookie(url));
            } catch (Exception e) {
                log.error("ParamCheckBiz.checkTaskUrl | 检查用户url是否正确时出现异常 url:{}, e = ", url, e);
                return false;
            }
        }
        return false;
    }

    /**
     * 检查用户添加任务时 post的学号是否正确
     * @param url 用户提交的url
     * @param schoolId 用户提交的学号
     * @return 检查结果
     */
    public boolean checkTaskSchoolId(String url, Long schoolId) {
        if (schoolId == null || schoolId <= 0) {
            return false;
        }
        try {
            userInfoBiz.getUserInfo(url, schoolId);
            return true;
        } catch (Exception e) {
            log.error("ParamCheckBiz.checkTaskSchoolId | 检查用户学号时出现异常 schoolId:{}, e = ", schoolId, e);
            return false;
        }
    }
}