package cn.hangcc.automaticpunchxust.service.AutomaticPunch;

import cn.hangcc.automaticpunchxust.domain.model.AutomaticPunch.UserInfoModel;

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
     * @return 实例对象
     */
    UserInfoModel insert(UserInfoModel userInfoModel);

    /**
     * 修改数据
     *
     * @param userInfoModel 实例对象
     * @return 实例对象
     */
    UserInfoModel update(UserInfoModel userInfoModel);
}