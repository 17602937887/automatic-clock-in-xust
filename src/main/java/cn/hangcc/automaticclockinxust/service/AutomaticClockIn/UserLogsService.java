/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.service.AutomaticClockIn;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/10
 */
public interface UserLogsService {
    /**
     * 新增数据
     *
     * @param schoolId 用户学号
     * @param name 用户姓名
     * @param info 存储信息
     * @return 影响行数
     */
    int insert(Long schoolId, String name, String info);
}