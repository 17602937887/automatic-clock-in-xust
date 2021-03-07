/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.biz.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.common.utils.AutomaticClockInUtils;
import cn.hangcc.automaticclockinxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ConfigModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.SmsLogsModel;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.ConfigService;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.SmsLogsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.http.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants.*;

/**
 * 阿里云短信服务biz
 *
 * @author chenhang
 * @created 2021/2/10
 */
@Slf4j
@Component
public class AliSmsBiz {

    @Resource
    private SmsLogsService smsLogsService;

    @Resource
    private ConfigService configService;

    /**
     * 通过用户注册成功
     * @param user 用户model
     */
    public void sendRegisterSuccessMsg(UserInfoModel user) {
        String phone = String.valueOf(user.getPhone());
        Map<String, String> map = new HashMap<>();
        map.put("name", user.getName());
        String templateParam = JSON.toJSONString(map);
        sendMsg(phone, ALIYUN_SMS_SIGN_NAME, ALIYUN_REGISTER_SUCCESS_TEMPLATE_CODE, templateParam);
    }

    /**
     * 通知用户打卡失败
     */
    public void sendClockInFailedMsg(ClockInMsgModel msg) {
        String phone = String.valueOf(msg.getPhone());
        Map<String, String> map = new HashMap<>();
        map.put("name", msg.getName());
        map.put("time", LocalDateUtils.getNowTime());
        String templateParam = JSON.toJSONString(map);
        sendMsg(phone, ALIYUN_SMS_SIGN_NAME, ALIYUN_CLOCK_IN_FAILED_TEMPLATE_CODE, templateParam);
    }

    /**
     * 发送异常信息到开发者、通知进行排查错误
     */
    public void sendExceptionToDev(String time, String exceptionMsg) {
        ConfigModel exceptionMessageReceiver = configService.query("exceptionMessageReceiver");
        List<String> receiverPhoneList = JSON.parseArray(exceptionMessageReceiver.getValue(), String.class);
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        map.put("errorMsg", exceptionMsg);
        String templateParam = JSON.toJSONString(map);
        receiverPhoneList.forEach(phone ->
                sendMsg(phone, ALIYUN_SMS_SIGN_NAME, ALIYUN_CLOCK_IN_EXCEPTION_TEMPLATE_CODE, templateParam));
    }

    /**
     * 发送消息封装方法
     * @param phone 发送的手机号
     * @param signName 签名
     * @param templateCode 模板id
     * @param templateParam 模板参数
     */
    private void sendMsg(String phone, String signName, String templateCode, String templateParam) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", AutomaticClockInConstants.ALIYUN_ACCESS_KEY_ID, AutomaticClockInConstants.ALIYUN_ACCESS_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", templateCode);
        try {
            CommonResponse response = client.getCommonResponse(request);
            SmsLogsModel smsLogsModel = new SmsLogsModel();
            smsLogsModel.setPhone(phone);
            smsLogsModel.setSignName(signName);
            smsLogsModel.setTemplateCode(templateCode);
            smsLogsModel.setTemplateParam(templateParam);
            smsLogsModel.setResultData(response.getData());
            InetAddress localHost = InetAddress.getLocalHost();
            String ip = AutomaticClockInUtils.getHostIp();
            String hostName = localHost.getHostName();
            smsLogsModel.setOtherInfo(String.format("机器IP:%s, 机器名称:%s", ip, hostName));
            smsLogsService.insert(smsLogsModel);
        } catch (ClientException | UnknownHostException e) {
            log.error("短信发送失败, e = ", e);
            sendExceptionToDev(LocalDateUtils.getNowTime(), e.getMessage().substring(0, 30));
            throw new RuntimeException();
        }
    }

}