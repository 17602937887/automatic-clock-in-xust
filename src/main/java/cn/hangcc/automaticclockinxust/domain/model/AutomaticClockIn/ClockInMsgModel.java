/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn;

import lombok.Data;

/**
 * 打卡所需的信息
 *
 * @author chenhang
 * @created 2021/2/10
 */
@Data
public class ClockInMsgModel {
    /**
     * 用户工号
     */
    private Long schoolId;
    /**
     * 用户的cookie
     */
    private String cookie;
    /**
     * 用户名字
     */
    private String name;
    /**
     * 用户手机号(必须)
     */
    private Long phone;
    /**
     * 当前签到的时间段 1:上午打卡 2.晚上打卡
     */
    private Integer status;
}