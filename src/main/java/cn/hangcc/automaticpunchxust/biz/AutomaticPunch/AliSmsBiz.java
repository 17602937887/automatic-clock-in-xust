/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.biz.AutomaticPunch;

import cn.hangcc.automaticpunchxust.common.constant.AutomaticPunchConstants;
import cn.hangcc.automaticpunchxust.domain.model.AutomaticPunch.UserInfoModel;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.http.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信服务biz
 *
 * @author chenhang
 * @created 2021/2/10
 */
@Slf4j
@Component
public class AliSmsBiz {
    public static void sendRegisterSuccessMsg(UserInfoModel user) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", AutomaticPunchConstants.ALIYUN_ACCESS_KEY_ID, AutomaticPunchConstants.ALIYUN_ACCESS_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", String.valueOf(user.getPhone()));
        request.putQueryParameter("SignName", AutomaticPunchConstants.ALIYUN_SMS_SIGN_NAME);
        request.putQueryParameter("TemplateCode", AutomaticPunchConstants.ALIYUN_TEMPLATE_CODE);
        request.putQueryParameter("TemplateParam", user.getName());
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("用户注册成功消息发送成功, name:{}, phone:{}", user.getName(), user.getPhone());
        } catch (ClientException e) {
            log.warn("AliSmsBiz.sendRegisterSuccessMsg | 用户注册成功短信发送失败, e = ", e);
        }
    }
}