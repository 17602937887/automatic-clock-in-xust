package cn.hangcc.automaticclockinxust.service.AutomaticClockIn.impl;

import cn.hangcc.automaticclockinxust.dao.AutomaticClockIn.SmsLogsDao;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.SmsLogsModel;
import cn.hangcc.automaticclockinxust.service.AutomaticClockIn.SmsLogsService;
import cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch.SmsLogsModelConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (AutomaticClockInSmsLogsTable)表服务实现类
 *
 * @author chenhang
 * @since 2021-03-06 20:16:57
 */
@Service("smsLogsService")
public class SmsLogsServiceImpl implements SmsLogsService {
    @Resource
    private SmsLogsDao smsLogsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SmsLogsModel queryById(Integer id) {
        return SmsLogsModelConverter.convertToSmsLogsModel(smsLogsDao.queryById(id));
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<SmsLogsModel> queryAllByLimit(int offset, int limit) {
        return smsLogsDao.queryAllByLimit(offset, limit)
                .stream()
                .map(SmsLogsModelConverter::convertToSmsLogsModel)
                .collect(Collectors.toList());
    }

    /**
     * 新增数据
     *
     * @param smsLogsModel 实例对象
     * @return 实例对象
     */
    @Override
    public SmsLogsModel insert(SmsLogsModel smsLogsModel) {
        smsLogsDao.insert(SmsLogsModelConverter.convertToSmsLogsDO(smsLogsModel));
        return smsLogsModel;
    }

    /**
     * 修改数据
     *
     * @param smsLogsModel 实例对象
     * @return 实例对象
     */
    @Override
    public SmsLogsModel update(SmsLogsModel smsLogsModel) {
        smsLogsDao.update(SmsLogsModelConverter.convertToSmsLogsDO(smsLogsModel));
        return queryById(smsLogsModel.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return smsLogsDao.deleteById(id) > 0;
    }
}