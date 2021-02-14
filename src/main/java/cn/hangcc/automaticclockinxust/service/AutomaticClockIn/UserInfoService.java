package cn.hangcc.automaticclockinxust.service.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;

import java.util.List;

/**
 * (AutomaticPunchUserInfoTable)表服务接口
 *
 * @author chenhang
 * @since 2021-02-05 18:44:46
 */
public interface UserInfoService {

    /**
     * 新增数据
     *
     * @param userInfoModel 实例对象
     */
    void insert(UserInfoModel userInfoModel);

    /**
     * 根据学号 查询用户在表中的数据
     * @param schoolId 学号
     * @return 该用户的基本信息
     */
    UserInfoModel query(Long schoolId);

    /**
     * 修改数据
     *
     * @param userInfoModel 实例对象
     */
    void update(UserInfoModel userInfoModel);

    /**
     * 查询所有早上需要打卡的用户
     * @return 用户集合
     */
    List<UserInfoModel> listMorningClockInUser();

    /**
     * 查询所有晚上需要打卡的用户
     * @return 用户集合
     */
    List<UserInfoModel> listEveningClockInUser();
}