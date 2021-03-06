package cn.hangcc.automaticclockinxust.service.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.SmsLogsModel;

import java.util.List;

/**
 * (AutomaticClockInSmsLogsTable)表服务接口
 *
 * @author chenhang
 * @since 2021-03-06 20:16:56
 */
public interface SmsLogsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SmsLogsModel queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SmsLogsModel> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param smsLogsModel 实例对象
     * @return 实例对象
     */
    SmsLogsModel insert(SmsLogsModel smsLogsModel);

    /**
     * 修改数据
     *
     * @param smsLogsModel 实例对象
     * @return 实例对象
     */
    SmsLogsModel update(SmsLogsModel smsLogsModel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}