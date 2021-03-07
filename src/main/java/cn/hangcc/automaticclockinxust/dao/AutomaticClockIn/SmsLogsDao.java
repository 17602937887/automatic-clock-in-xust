package cn.hangcc.automaticclockinxust.dao.AutomaticClockIn;

import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.SmsLogsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (AutomaticClockInSmsLogsTable)表数据库访问层
 *
 * @author chenhang
 * @since 2021-03-06 20:16:55
 */
@Repository
public interface SmsLogsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SmsLogsDO queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SmsLogsDO> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param smsLogsDO 实例对象
     * @return 对象列表
     */
    List<SmsLogsDO> queryAll(SmsLogsDO smsLogsDO);

    /**
     * 新增数据
     *
     * @param smsLogsDO 实例对象
     * @return 影响行数
     */
    int insert(SmsLogsDO smsLogsDO);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AutomaticClockInSmsLogsTable> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SmsLogsDO> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AutomaticClockInSmsLogsTable> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SmsLogsDO> entities);

    /**
     * 修改数据
     *
     * @param smsLogsDO 实例对象
     * @return 影响行数
     */
    int update(SmsLogsDO smsLogsDO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}