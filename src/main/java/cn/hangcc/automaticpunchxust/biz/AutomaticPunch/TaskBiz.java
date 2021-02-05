/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.biz.AutomaticPunch;

import cn.hangcc.automaticpunchxust.domain.model.AutomaticPunch.UserInfoModel;
import org.springframework.stereotype.Component;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
@Component
public class TaskBiz {

    /**
     * 根据用户提交的url 返回该用户对应的model
     * @param url 用户提交的url
     * @return 该用户对应的model
     */
    public UserInfoModel getUserInfoModel(String url) {
        return getUserInfoModel(url, null);
    }

    /**
     * 根据用户提交的url 返回该用户对应的model
     * @param url 用户提交的url
     * @param email 用户提交的邮箱
     * @return 该用户对应的model
     */
    public UserInfoModel getUserInfoModel(String url, String email) {
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUserUrl(url);
        userInfoModel.setEmail(email);

        return userInfoModel;
    }
}