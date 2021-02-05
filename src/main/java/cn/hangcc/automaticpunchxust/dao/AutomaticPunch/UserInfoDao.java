package cn.hangcc.automaticpunchxust.dao.AutomaticPunch;

import cn.hangcc.automaticpunchxust.domain.dto.AutomaticPunch.UserInfoDO;

/**
 * (AutomaticPunchUserInfoTable)表数据库访问层
 *
 * @author chenhang
 * @since 2021-02-05 18:44:44
 */
public interface UserInfoDao {
    /**
     * 新增数据
     *
     * @param userInfoDO 实例对象
     * @return 影响行数
     */
    int insert(UserInfoDO userInfoDO);

    /**
     * 修改数据
     *
     * @param userInfoDO 实例对象
     * @return 影响行数
     */
    int update(UserInfoDO userInfoDO);
}