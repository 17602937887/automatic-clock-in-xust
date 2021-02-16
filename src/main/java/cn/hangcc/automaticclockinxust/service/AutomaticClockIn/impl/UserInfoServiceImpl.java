package cn.hangcc.automaticclockinxust.service.AutomaticClockIn.impl;

import cn.hangcc.automaticclockinxust.dao.AutomaticClockIn.UserInfoDao;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.UserInfoModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.UserInfoService;
import cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch.UserInfoModelConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 查询所有早上需要打卡的用户
     * @return 用户集合
     */
    @Override
    public List<UserInfoModel> listMorningClockInUser() {
        return userInfoDao.listMorningClockInUser()
                .stream()
                .map(UserInfoModelConverter::convertToUserInfoModel)
                .collect(Collectors.toList());
    }

    /**
     * 查询所有晚上需要打卡的用户
     * @return 用户集合
     */
    @Override
    public List<UserInfoModel> listEveningClockInUser() {
        return userInfoDao.listEveningClockInUser()
                .stream()
                .map(UserInfoModelConverter::convertToUserInfoModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInfoModel> listAllUser() {
        return userInfoDao.listAllUser()
                .stream()
                .map(UserInfoModelConverter::convertToUserInfoModel)
                .collect(Collectors.toList());
    }
}