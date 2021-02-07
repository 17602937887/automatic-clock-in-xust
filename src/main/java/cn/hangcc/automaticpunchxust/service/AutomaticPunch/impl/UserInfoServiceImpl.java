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
     */
    @Override
    public void insert(UserInfoModel userInfoModel) {
        if ((query(userInfoModel.getSchoolId()) == null)) {
            userInfoDao.insert(UserInfoModelConverter.convertToUserInfoDO(userInfoModel));
        } else {
            userInfoDao.update(UserInfoModelConverter.convertToUserInfoDO(userInfoModel));
        }
    }

    /**
     * 根据学号 查询用户在表中的数据
     * @param schoolId 学号
     */
    @Override
    public UserInfoModel query(Long schoolId) {
        return UserInfoModelConverter.convertToUserInfoModel(userInfoDao.query(schoolId));
    }

    /**
     * 修改数据
     *
     * @param userInfoModel 实例对象
     */
    @Override
    public void update(UserInfoModel userInfoModel) {
        userInfoDao.update(UserInfoModelConverter.convertToUserInfoDO(userInfoModel));
    }
}