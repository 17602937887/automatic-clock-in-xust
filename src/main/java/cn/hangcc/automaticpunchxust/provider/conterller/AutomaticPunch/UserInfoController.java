/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.provider.conterller.AutomaticPunch;

import cn.hangcc.automaticpunchxust.biz.AutomaticPunch.ParamCheckBiz;
import cn.hangcc.automaticpunchxust.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private ParamCheckBiz paramCheckBiz;

    @PostMapping("/add.json")
    public ApiResponse addUser(@RequestParam("url") String url, @RequestParam(value = "email", required = false) String email) {
        try {
            // 参数检查
            if (!paramCheckBiz.checkAddTaskUrl(url)) {
                return ApiResponse.buildFailure("Url错误,请检查!");
            }
        } catch (Exception e) {
            log.error("UserInfoController.addUser | 用户添加任务时出现异常, url:{}, e=", url, e);
            return ApiResponse.buildFailure();
        }
    }
}