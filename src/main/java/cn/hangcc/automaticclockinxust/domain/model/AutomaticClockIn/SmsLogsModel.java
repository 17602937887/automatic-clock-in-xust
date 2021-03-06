package cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (AutomaticClockInSmsLogsTable)实体类
 *
 * @author chenhang
 * @since 2021-03-06 20:16:55
 */
@Data
public class SmsLogsModel implements Serializable {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 签名
     */
    private String signName;
    /**
     * 模板id
     */
    private String templateCode;
    /**
     * 模板参数
     */
    private String templateParam;
    /**
     * 调用返回信息
     */
    private String resultData;
    /**
     * 其他信息
     */
    private String otherData;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;
}