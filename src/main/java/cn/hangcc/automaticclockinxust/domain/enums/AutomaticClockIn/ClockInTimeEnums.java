/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.domain.enums.AutomaticClockIn;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/10
 */
public enum ClockInTimeEnums {
    /**
     * 早上打卡
     */
    MORNING(1, "早上打卡"),

    /**
     * 晚上打卡
     */
    EVENING(2, "晚上打卡")
    ;

    /**
     * 打卡状态 1.为早上 2.为晚上
     */
    private Integer status;

    /**
     * 描述
     */
    private String desc;

    ClockInTimeEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}