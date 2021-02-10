/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.biz.AutomaticPunch;

import cn.hangcc.automaticpunchxust.common.constant.AutomaticPunchConstants;
import cn.hangcc.automaticpunchxust.common.utils.AutomaticPunchUtils;
import cn.hangcc.automaticpunchxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticpunchxust.domain.enums.AutomaticClockIn.ClockInTimeEnums;
import cn.hangcc.automaticpunchxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
@Slf4j
@Component
public class TaskBiz {

    /**
     * 执行打卡任务
     * @param msg kafka接到的消息
     * @return 是否成功
     */
    public boolean executeTask(ClockInMsgModel msg) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = AutomaticPunchUtils.setHeaders(new HttpGet(AutomaticPunchConstants.REQUEST_USER_INFO_URL + msg.getSchoolId()));
        request.setHeader("cookie", msg.getCookie());
        String jsonStr = EntityUtils.toString(client.execute(request).getEntity(), StandardCharsets.UTF_8);
        // 判断是否已经打过卡了 如果已经打过的话 直接消费掉
        if (checkHasBeenPerformed(jsonStr, msg)) {
            return true;
        }
        try {
            // 获取到的上次进行打卡的所有数据
            JSONObject prePostData = JSONObject.parseObject(jsonStr).getJSONArray("list").getJSONObject(0);
            // 本次进行发送post请求的json对象
            JSONObject clockInData = (JSONObject) prePostData.clone();
            JSONObject postJsonData = constructPostParam(prePostData, clockInData, msg.getStatus());
            HttpPost httpPost = new HttpPost(AutomaticPunchConstants.CLOCK_IN_POST_URL);
            AutomaticPunchUtils.setHeaders(httpPost);
            httpPost.setHeader("cookie", msg.getCookie());
            httpPost.setHeader("Referer", String.valueOf(msg.getSchoolId()));
            StringEntity entity = new StringEntity(postJsonData.toJSONString(), "application/json", "utf-8");
            httpPost.setEntity(entity);
            client.execute(httpPost);
            return true;
        } catch (Exception e) {
            log.error("TaskBiz.executeTask | 执行打卡操作时出现异常, msg:{}, e = ", msg, e);
            return false;
        }
    }

    /**
     * 接到的消息 判断该时段该用户是否已经打过卡了
     * @param jsonStr 根据用户的学号进行请求上次打卡获取到的数据
     * @param msg kafka接到的消息
     * @return true或者false true代表打过，false代表没打
     */
    private boolean checkHasBeenPerformed(String jsonStr, ClockInMsgModel msg) throws IOException {
        LocalDateTime lastDateTime = getLastTime(jsonStr);
        // 当前日期yyyyMMdd
        String nowDate = LocalDate.now().format(LocalDateUtils.INT_DATE_FORMATTER);
        // 上次打卡日期yyyyMMdd
        String lastDate = lastDateTime.toLocalDate().format(LocalDateUtils.INT_DATE_FORMATTER);
        return nowDate.equals(lastDate);
    }

    /**
     * 获取上次打卡的时间 并转为LocalDateTime
     * @param jsonStr 获取到的上次打卡的json数据
     * @return 上次打卡的时间LocalDateTime
     */
    private LocalDateTime getLastTime(String jsonStr) {
        String str = JSONObject.parseObject(jsonStr).getJSONArray("list").getJSONObject(0).getString("TBSJ");
        return LocalDateTime.parse(str, LocalDateUtils.LEGACY_DATE_TIME_FORMATTER);
    }

    /**
     * 构造本次请求的所有请求参数
     * @param prePostData 上次的请求数据
     * @param clockInData 本次即将构造的请求数据
     * @param status 打卡状态 1.为上午打卡 2.为晚上打卡
     */
    private JSONObject constructPostParam(JSONObject prePostData, JSONObject clockInData, Integer status) {
        clockInData.put("dm", prePostData.getString("BJDM"));
        clockInData.put("jrtwfw5", prePostData.get("jrtwfw5".toUpperCase()));
        clockInData.put("xxdz41", prePostData.getString("XXDZ4_1"));
        for (String key : prePostData.keySet()) {
            clockInData.put(key.toLowerCase(), prePostData.getString(key));
        }
        Set<String> clockInDateSets = clockInData.keySet();
        for (String key : clockInDateSets) {
            if (key.equals(key.toUpperCase())) {
                clockInData.remove(key);
            }
        }
        clockInData.remove("xxdz4_1");
        // 早上打卡还是当天 晚上打卡变成了下一天
        clockInData.put("jrrq1", status.equals(ClockInTimeEnums.MORNING.getStatus()) ? LocalDateUtils.getTodayDate() : LocalDateUtils.getNextDate());
        clockInData.put("tbsj", LocalDateUtils.getNowTime());
        clockInData.put("time", LocalDateUtils.getNowTime());
        clockInData.put("guo", "中国");
        clockInData.put("procinstid", "");
        clockInData.put("id", "");
        clockInData.put("glkssj131", "");
        clockInData.put("gljssj132", "");
        clockInData.put("qtxx15", null);
        clockInData.put("sfzh", "");
        clockInData.put("zy", "");
        clockInData.put("zydm", "");
        clockInData.put("jg", "");
        clockInData.put("yx", "");
        clockInData.put("fcjtgj17Qt", "");
        clockInData.put("fcjtgj17", "");
        clockInData.put("ymtys", "");
        //  这里一定要加 早上打的数据是 1， 晚上打的数据是0， 鬼知道这个玩意的开发者定义啥意思
        clockInData.put("jdlx", status.equals(ClockInTimeEnums.MORNING.getStatus()) ? "1" : "0");
        JSONObject postJsonData = new JSONObject();
        postJsonData.put("xkdjkdk", clockInData);
        return postJsonData;
    }
}