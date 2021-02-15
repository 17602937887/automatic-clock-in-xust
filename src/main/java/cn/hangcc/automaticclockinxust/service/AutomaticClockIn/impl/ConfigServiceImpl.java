/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.service.AutomaticClockIn.impl;

import cn.hangcc.automaticclockinxust.dao.AutomaticClockIn.ConfigDao;
import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.ConfigDO;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ConfigModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.ConfigService;
import cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch.ConfigModelConverter;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/11
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigDao configDao;

    /**
     * 根据key查询对应的value值 类似于统一配置中心
     * @param key 键
     * @return 值
     */
    @Override
    public ConfigModel query(String key) {
        return ConfigModelConverter.convertToConfigModel(configDao.query(key));
    }
}