/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.biz.AutomaticPunch;

import cn.hangcc.automaticpunchxust.common.constant.AutomaticPunchConstants;
import cn.hangcc.automaticpunchxust.common.utils.AutomaticPunchUtils;
import cn.hangcc.automaticpunchxust.domain.model.AutomaticClockIn.UserInfoModel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/6
 */
@Slf4j
@Component
public class UserInfoBiz {

    /**
     * 根据用户提供的URL 获取用户的Cookie
     * @param url 用户提供的Url
     * @return 该用户对应的Cookie
     * @throws IOException
     */
    public String getUserCookie(String url) throws IOException {
        try {
            // 创建client
            CloseableHttpClient client = HttpClientBuilder.create().build();
            // 创建请求 并设置headers
            HttpGet request = AutomaticPunchUtils.setHeaders(new HttpGet(url));
            // 执行http请求
            CloseableHttpResponse response = client.execute(request);
            Header[] headers = response.getHeaders("Set-Cookie");
            return headers[0].getValue();
        } catch (Exception e) {
            log.error("UserInfoBiz.getUserCookie | 根据用户提供的URL获取Cookie时出现异常 url:{} e = ", url, e);
            return "";
        }
    }

    /**
     * 根据用户提供的信息 获取用户信息Model
     * @param url 用户的签到打卡地址
     * @param schoolId 学号
     * @return 用户信息Model
     */
    public UserInfoModel getUserInfo(String url, Long schoolId) {
        try {
            // 创建client
            CloseableHttpClient client = HttpClientBuilder.create().build();
            // 设置请求地址及Headers
            HttpGet request = AutomaticPunchUtils.setHeaders(new HttpGet(String.format("%s%d", AutomaticPunchConstants.REQUEST_USER_INFO_URL, schoolId)));
            // 设置Cookie
            String cookie = getUserCookie(url);
            request.setHeader("Cookie", cookie);
            // 发起请求 得到请求结果
            CloseableHttpResponse response = client.execute(request);
            // 转为json
            String jsonResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            // 获取基本信息的map
            Map<String, String> map = (Map<String, String>) JSON.parseObject(jsonResponse).getJSONArray("list").get(0);
            UserInfoModel user = new UserInfoModel();
            setUserInfoAttribute(user, map);
            user.setCookie(cookie);
            return user;
        } catch (Exception e) {
            log.error("UserInfoBiz.getUserInfo | 根据用户提供的信息获取用户基本信息 url:{}, schoolId:{}, e = ", url, schoolId, e);
            throw new RuntimeException("用户基本信息获取失败");
        }
    }

    private void setUserInfoAttribute(UserInfoModel user, Map<String, String> map) {
        user.setName(map.get("XM"));
        user.setPhone(Long.valueOf(map.get("SJH2")));
        user.setGender(map.get("XB"));
        user.setClassName(map.get("BJ"));
        user.setCollege(map.get("SZYX"));
    }
}