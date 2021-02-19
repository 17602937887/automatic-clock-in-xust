/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch;

import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.UserInfoDO;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;

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
        userInfoModel.setUserUrl(userInfoDO.getUserUrl());
        userInfoModel.setCookie(userInfoDO.getCookie());
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
        userInfoDO.setCookie(userInfoModel.getCookie());
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

    /**
     * Convert ClockInMsgModel to UserInfoModel
     * @param clockInMsgModel
     * @return
     */
    public static UserInfoModel convertToUserInfoModel(ClockInMsgModel clockInMsgModel) {
        if (clockInMsgModel == null) {
            return null;
        }
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setSchoolId(clockInMsgModel.getSchoolId());
        userInfoModel.setUserUrl(clockInMsgModel.getUrl());
        userInfoModel.setName(clockInMsgModel.getName());
        userInfoModel.setPhone(clockInMsgModel.getPhone());
        userInfoModel.setStatus(clockInMsgModel.getStatus());

        return userInfoModel;
    }

    /**
     * Convert UserInfoModel to ClockInMsgModel
     * @param userInfoModel
     * @return
     */
    public static ClockInMsgModel convertToClockInMsgModel(UserInfoModel userInfoModel) {
        if (userInfoModel == null) {
            return null;
        }
        ClockInMsgModel clockInMsgModel = new ClockInMsgModel();

        clockInMsgModel.setSchoolId(userInfoModel.getSchoolId());
        clockInMsgModel.setUrl(userInfoModel.getUserUrl());
        clockInMsgModel.setName(userInfoModel.getName());
        clockInMsgModel.setPhone(userInfoModel.getPhone());
        clockInMsgModel.setStatus(userInfoModel.getStatus());

        return clockInMsgModel;
    }
}
