package cn.hangcc.automaticclockinxust.dao.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.UserInfoDO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 查询所有早上需要打卡的用户
     * @return 用户集合
     */
    List<UserInfoDO> listMorningClockInUser();

    /**
     * 查询所有晚上需要打卡的用户
     * @return 用户集合
     */
    List<UserInfoDO> listEveningClockInUser();

    /**
     * 查询所有的用户集合
     * @return 用户集合
     */
    List<UserInfoDO> listAllUser();
}