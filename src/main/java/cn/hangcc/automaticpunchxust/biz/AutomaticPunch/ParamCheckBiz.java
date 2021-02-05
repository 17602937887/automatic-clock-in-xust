/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.biz.AutomaticPunch;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/5
 */
@Component
public class ParamCheckBiz {
    /**
     * 检查用户添加任务时 post的Url是否正确
     * @param url 用户提交的url
     * @return 该url是否正确
     */
    public boolean checkAddTaskUrl(String url) {
        if (StringUtils.hasLength(url)) {

        }
        return false;
    }
}