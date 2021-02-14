/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.service.AutomaticClockIn.impl;

import cn.hangcc.automaticclockinxust.dao.AutomaticClockIn.UserLogsDao;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserLogsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/10
 */
@Service
public class UserLogsServiceImpl implements UserLogsService {

    @Resource
    private UserLogsDao userLogsDao;

    /**
     * 新增数据
     *
     * @param schoolId 用户学号
     * @param name 用户姓名
     * @param info 存储信息
     * @return 影响行数
     */
    @Override
    public int insert(Long schoolId, String name, String info) {
        return userLogsDao.insert(schoolId, name, info);
    }
}