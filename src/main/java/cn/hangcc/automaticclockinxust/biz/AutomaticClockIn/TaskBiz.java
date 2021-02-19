/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.biz.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.common.constant.AutomaticClockInConstants;
import cn.hangcc.automaticclockinxust.common.utils.AutomaticClockInUtils;
import cn.hangcc.automaticclockinxust.common.utils.LocalDateUtils;
import cn.hangcc.automaticclockinxust.domain.enums.AutomaticClockIn.ClockInTimeEnums;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ClockInMsgModel;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
@Slf4j
@Component
public class TaskBiz {

    @Resource
    private UserInfoBiz userInfoBiz;

    /**
     * 执行打卡任务
     * @param msg kafka接到的消息
     * @return 是否成功
     */
    public boolean executeTask(ClockInMsgModel msg) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = AutomaticClockInUtils.setHeaders(new HttpGet(AutomaticClockInConstants.REQUEST_USER_INFO_URL + msg.getSchoolId()));
        String cookie = userInfoBiz.getUserCookie(msg.getUrl());
        request.setHeader("cookie", cookie);
        String jsonStr = EntityUtils.toString(client.execute(request).getEntity(), StandardCharsets.UTF_8);
        // 判断是否已经打过卡了 如果已经打过的话 直接消费掉
//        if (checkHasBeenPerformed(jsonStr)) {
//            return true;
//        }
        try {
            // 获取到的上次进行打卡的所有数据
            JSONObject prePostData = JSONObject.parseObject(jsonStr).getJSONArray("list").getJSONObject(0);
            // 本次进行发送post请求的json对象
            JSONObject clockInData = (JSONObject) prePostData.clone();
            JSONObject postJsonData = constructPostParam(prePostData, clockInData, msg.getStatus());
            HttpPost httpPost = new HttpPost(AutomaticClockInConstants.CLOCK_IN_POST_URL);
            AutomaticClockInUtils.setHeaders(httpPost);
            httpPost.setHeader("cookie", cookie);
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
     * @return true或者false true代表打过，false代表没打
     */
    private boolean checkHasBeenPerformed(String jsonStr) {
        LocalDateTime lastDateTime = getLastTime(jsonStr);
        // 当前日期yyyyMMdd
        int nowDate = LocalDateUtils.getInt(LocalDate.now());
        // 上次打卡日期yyyyMMdd
        int lastDate = LocalDateUtils.getInt(lastDateTime.toLocalDate());
        int lastStatus = getLastStatus(jsonStr);
        // 当前是早上
        if (LocalTime.now().isBefore(LocalDateUtils.getLocalTime(AutomaticClockInConstants.TIME_SEPARATION_POINT))) {
            return (nowDate == lastDate && lastStatus == 1);
        }
        return (nowDate == lastDate && lastStatus == 0);
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
     * 获取上次打卡的状态 若为1 则代表早上打卡 若为0则为晚上打卡
     * @param jsonStr
     * @return
     */
    private int getLastStatus(String jsonStr) {
        return Integer.parseInt(JSONObject.parseObject(jsonStr).getJSONArray("list").getJSONObject(0).getString("JDLX"));
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
        List<String> clockInDateSets = new ArrayList<>(clockInData.keySet());
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