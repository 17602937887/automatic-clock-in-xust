/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.common.constant;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/6
 */
public class AutomaticClockInConstants {
    /**
     * 通过用户工号获取用户基本信息的URL
     */
    public static final String REQUEST_USER_INFO_URL = "https://ehallplatform.xust.edu.cn/default/jkdk/mobile/com.primeton.eos.jkdk.xkdjkdkbiz.getJkdkRownum.biz.ext?gh=";

    /**
     * 进行打卡数据post的URL地址
     */
    public static final String CLOCK_IN_POST_URL = "http://ehallplatform.xust.edu.cn/default/jkdk/mobile/com.primeton.eos.jkdk.xkdjkdkbiz.jt.biz.ext";

    /**
     * kafka签到信息的topic. 此处更换为你创建的topic名称
     */
    public static final String KAFKA_CLOCK_IN_INFO_TOPIC = "clock-in-info";

    /**
     * kafka发送注册成功短信的topic，此处更换为你创建的topic名称
     */
    public static final String KAFKA_SEND_REGISTER_SUCCESS_SMS_TOPIC = "register-success-topic";
    /**
     * kafka发生打卡失败短信的topic。
     */
    public static final String KAFKA_SEND_CLOCK_IN_FAILED_SMS_TOPIC = "clock-in-failed-topic";
    /**
     * 阿里云accessKeyId. 此处更换为你在阿里云accessKeyId
     */
    public static final String ALIYUN_ACCESS_KEY_ID = "LTAI4G49PnQjhCZ2kb8GwWNT";

    /**
     * 阿里云accessSecret. 此处更换为你在阿里云accessSecret
     */
    public static final String ALIYUN_ACCESS_SECRET = "zonhvecFfoQthKjYg62JMNPjHDFYrP";

    /**
     * 阿里云短信服务签名. 此处更改为你在阿里云的签名
     */
    public static final String ALIYUN_SMS_SIGN_NAME = "陈航";

    /**
     * 注册成功的阿里云短信服务短信模板id. 此处更改为你在阿里云的短信模板id.
     */
    public static final String ALIYUN_REGISTER_SUCCESS_TEMPLATE_CODE = "SMS_190793505";

    /**
     * 打卡失败后的阿里云短信服务短信模板id. 此处更改为你在阿里云的短信模板id.
     */
    public static final String ALIYUN_CLOCK_IN_FAILED_TEMPLATE_CODE = "SMS_190782739";

    /**
     * 打卡失败后 重试次数
     */
    public static final Integer CLOCK_IN_FAILED_RETRY_COUNT = 3;

    /**
     * 签到成功落库的文案
     */
    public static final String CLOCK_IN_SUCCESS_CONTENT = "签到成功";

    /**
     * 签到失败落库的文案
     */
    public static final String CLOCK_IN_FAILED_CONTENT = "签到失败";

    /**
     * 签到失败并发送短信提醒的落库文案
     */
    public static final String CLOCK_IN_FAILED_SEND_SMS_CONTENT = "签到失败、成功发送短信提醒";

    /**
     * 签到失败 catch到异常的落库文案
     */
    public static final String CLOCK_IN_FAILED_EXCEPTION = "签到失败、出现异常";

    /**
     * 保证配置中心起始的时间也会进行签到操作
     */
    public static final long KAFKA_SLEEP_TIME = 2000;
}