package cn.hangcc.automaticpunchxust.domain.dto.AutomaticPunch;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author chenhang
 * @since 2021-02-05 18:44:43
 */
@Data
public class UserInfoDO implements Serializable {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户工号
     */
    private Long schoolId;
    /**
     * 用户的签到地址
     */
    private String userUrl;
    /**
     * 用户名字
     */
    private String name;
    /**
     * 用户手机号(必须)
     */
    private Long phone;
    /**
     * 用户邮箱(可选)
     */
    private String email;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 用户班级
     */
    private String className;
    /**
     * 用户所在学院
     */
    private String college;
    /**
     * 用户状态 1:需要早上签到 2.需要打晚上签到 3.两次都需要 4.用户状态无效
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;
}