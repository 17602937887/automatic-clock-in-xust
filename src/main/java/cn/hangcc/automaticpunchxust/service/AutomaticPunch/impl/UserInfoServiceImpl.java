package cn.hangcc.automaticpunchxust.service.AutomaticPunch.impl;

import cn.hangcc.automaticpunchxust.dao.AutomaticPunch.UserInfoDao;
import cn.hangcc.automaticpunchxust.domain.model.AutomaticPunch.UserInfoModel;
import cn.hangcc.automaticpunchxust.service.AutomaticPunch.UserInfoService;
import cn.hangcc.automaticpunchxust.service.converter.AutomaticPunch.UserInfoModelConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (AutomaticPunchUserInfoTable)表服务实现类
 *
 * @author chenhang
 * @since 2021-02-05 18:44:46
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;

    /**
     * 新增数据
     *
     * @param userInfoModel 实例对象
     * @return 实例对象
     */
    @Override
    public UserInfoModel insert(UserInfoModel userInfoModel) {
        userInfoDao.insert(UserInfoModelConverter.convertToUserInfoDO(userInfoModel));
        return userInfoModel;
    }

    /**
     * 修改数据
     *
     * @param userInfoModel 实例对象
     * @return 实例对象
     */
    @Override
    public UserInfoModel update(UserInfoModel userInfoModel) {
        userInfoDao.update(UserInfoModelConverter.convertToUserInfoDO(userInfoModel));
        return userInfoModel;
    }
}