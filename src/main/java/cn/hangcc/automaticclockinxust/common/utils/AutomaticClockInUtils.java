/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/6
 */
@Slf4j
public class AutomaticClockInUtils {
    /**
     * 对请求设置请求头 防止反爬措施的制裁
     *
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

    public static HttpPost setHeaders(HttpPost httpPost) {
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        httpPost.setHeader("Accept", "*/*");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Content-Type", "text/json");
        httpPost.setHeader("Host", "ehallplatform.xust.edu.cn");
        httpPost.setHeader("Origin", "https://ehallplatform.xust.edu.cn");
        httpPost.setHeader("Sec-Fetch-Dest", "empty");
        httpPost.setHeader("Sec-Fetch-Mode", "cors");
        httpPost.setHeader("Sec-Fetch-Site", "same-origin");
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        return httpPost;
    }

    /**
     * 获取机器ip
     *
     * @return HostIp
     */
    public static String getHostIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取机器ip时出现异常:e = ", e);
        }
        return "机器ip获取失败";
    }
}