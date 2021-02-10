/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.common.constant;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/6
 */
public class AutomaticPunchConstants {
    /**
     * 通过用户工号获取用户基本信息的URL
     */
    public static final String REQUEST_USER_INFO_URL = "https://ehallplatform.xust.edu.cn/default/jkdk/mobile/com.primeton.eos.jkdk.xkdjkdkbiz.getJkdkRownum.biz.ext?gh=";

    /**
     * kafka签到信息的topic. 此处更换为你创建的topic名称
     */
    public static final String KAFKA_PUNCH_INFO_TOPIC = "xust";

    /**
     * 阿里云accessKeyId. 此处更换为你在阿里云accessKeyId
     */
    public static final String ALIYUN_ACCESS_KEY_ID = "LTAIlHdZZxWKDTW3";

    /**
     * 阿里云accessSecret. 此处更换为你在阿里云accessSecret
     */
    public static final String ALIYUN_ACCESS_SECRET = "yS9KudRRWfBTxzNywTBPPsxYBuVhqh";

    /**
     * 阿里云短信服务签名. 此处更改为你在阿里云的签名
     */
    public static final String ALIYUN_SMS_SIGN_NAME = "科大自动健康打卡";

    /**
     * 阿里云短信服务短信模板id. 此处更改为你在阿里云的短信模板id.
     */
    public static final String ALIYUN_TEMPLATE_CODE = "SMS_190793505";
}