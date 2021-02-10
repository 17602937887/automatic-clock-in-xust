/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticpunchxust.dao.AutomaticPunch;

import cn.hangcc.automaticpunchxust.domain.dto.AutomaticPunch.UserInfoDO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * 在这里编写类的功能描述
 *
 * @author chenhang
 * @created 2021/2/10
 */
@Repository
public interface UserLogsDao {
    /**
     * 新增数据
     *
     * @param schoolId 用户学号
     * @param name 用户姓名
     * @param info 存储信息
     * @return 影响行数
     */
    int insert(@Param("schoolId") Long schoolId,@Param("name") String name,@Param("info") String info);
}