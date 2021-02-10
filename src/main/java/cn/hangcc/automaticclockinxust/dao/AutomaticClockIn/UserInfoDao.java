package cn.hangcc.automaticclockinxust.dao.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.UserInfoDO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * (AutomaticPunchUserInfoTable)表数据库访问层
 *
 * @author chenhang
 * @since 2021-02-05 18:44:44
 */
@Repository
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

    /**
     * 查询数据
     * @param schoolId 学号
     * @return 用户的信息
     */
    UserInfoDO query(@Param("schoolId") Long schoolId);
}