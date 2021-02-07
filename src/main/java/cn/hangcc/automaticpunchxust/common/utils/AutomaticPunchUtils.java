/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.common.utils;

import org.apache.http.client.methods.HttpGet;
import org.apache.tomcat.jni.OS;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/6
 */
public class AutomaticPunchUtils {
    /**
     * 对请求设置请求头 防止反爬措施的制裁
     * @param request 请求
     */
    public static HttpGet setHeaders(HttpGet request) {
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.setHeader("Host", "ehallplatform.xust.edu.cn");
        request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36");
        return request;
    }
}