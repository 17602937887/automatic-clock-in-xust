/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.service.converter.AutomaticPunch;

import cn.hangcc.automaticpunchxust.domain.dto.AutomaticPunch.UserInfoDO;
import cn.hangcc.automaticpunchxust.domain.model.AutomaticPunch.UserInfoModel;

/**
 *
 * @author chenhang
 * @version $Id: UserInfoModelConverter.java, v 0.1 2021-02-05 18:51:11 chenhang Exp $$
 */
public class UserInfoModelConverter {

    /**
     * Convert UserInfoDO to UserInfoModel
     * @param userInfoDO
     * @return
     */
    public static UserInfoModel convertToUserInfoModel(UserInfoDO userInfoDO) {
        if (userInfoDO == null) {
            return null;
        }
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setId(userInfoDO.getId());
        userInfoModel.setSchoolId(userInfoDO.getSchoolId());
        userInfoModel.setUserUrl(userInfoModel.getUserUrl());
        userInfoModel.setName(userInfoDO.getName());
        userInfoModel.setPhone(userInfoDO.getPhone());
        userInfoModel.setEmail(userInfoDO.getEmail());
        userInfoModel.setGender(userInfoDO.getGender());
        userInfoModel.setClassName(userInfoDO.getClassName());
        userInfoModel.setCollege(userInfoDO.getCollege());
        userInfoModel.setStatus(userInfoDO.getStatus());
        userInfoModel.setCreated(userInfoDO.getCreated());
        userInfoModel.setUpdated(userInfoDO.getUpdated());

        return userInfoModel;
    }

    /**
     * Convert UserInfoModel to UserInfoDO
     * @param userInfoModel
     * @return
     */
    public static UserInfoDO convertToUserInfoDO(UserInfoModel userInfoModel) {
        if (userInfoModel == null) {
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();

        userInfoDO.setId(userInfoModel.getId());
        userInfoDO.setSchoolId(userInfoModel.getSchoolId());
        userInfoDO.setUserUrl(userInfoModel.getUserUrl());
        userInfoDO.setName(userInfoModel.getName());
        userInfoDO.setPhone(userInfoModel.getPhone());
        userInfoDO.setEmail(userInfoModel.getEmail());
        userInfoDO.setGender(userInfoModel.getGender());
        userInfoDO.setClassName(userInfoModel.getClassName());
        userInfoDO.setCollege(userInfoModel.getCollege());
        userInfoDO.setStatus(userInfoModel.getStatus());
        userInfoDO.setCreated(userInfoModel.getCreated());
        userInfoDO.setUpdated(userInfoModel.getUpdated());

        return userInfoDO;
    }
}
