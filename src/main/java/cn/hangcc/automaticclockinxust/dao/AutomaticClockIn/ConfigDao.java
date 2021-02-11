/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.dao.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.ConfigDO;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/11
 */
public interface ConfigDao {
    /**
     * 根据key查询对应的value值 类似于统一配置中心
     * @param key 键
     * @return 值
     */
    ConfigDO query(@Param("key") String key);
}