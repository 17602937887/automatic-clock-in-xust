# 项目需要的库、表、等结构的sql语句

# 库名
create database if not exists automatic_clock_in_xust;

# 用户信息表
create table if not exists automatic_clock_in_user_info_table(
    `id` int(11) primary key auto_increment comment '主键',
    `school_id` bigint(20) not null comment '用户工号',
    `user_url` varchar(255) not null comment '用户进行签到的URL地址',
    `cookie` varchar(255) not null comment '用户的cookie',
    `name` varchar(255) not null comment '用户名字',
    `phone` bigint(20) not null comment '用户手机号(必须)',
    `email` varchar(255) comment '用户邮箱(可选)',
    `gender` varchar(255) not null comment '用户性别',
    `class_name` varchar(255) not null comment '用户班级',
    `college` varchar(255) not null comment '用户所在学院',
    `status` int(11) not null default 3 comment '用户状态 1:需要早上签到 2.需要打晚上签到 3.两次都需要 4.用户状态无效',
    `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    unique key uk_school_id(`school_id`) comment '唯一索引 工号',
    key idx_status(`status`) comment '普通索引'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 用户打卡日志表
create table if not exists automatic_clock_in_user_logs_table(
    `id` int(11) primary key auto_increment comment '主键',
    `school_id` bigint(20) not null comment '用户工号',
    `name` varchar(255) not null comment '用户名字',
    `info` varchar(255) comment '日志信息',
    `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 系统配置表 统一配置中心
create table if not exists automatic_clock_in_config_table(
    `id` int(11) primary key auto_increment comment '主键',
    `key` varchar(255) not null comment '键',
    `value` varchar(255) not null comment '值',
    `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    key inx_key(`key`) comment '普通索引'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 插入早上开始签到的时间点
insert into automatic_clock_in_config_table(`key`, `value`) values ('morningStartTime', '11:15:00');
# 插入早上结束签到的时间点
insert into automatic_clock_in_config_table(`key`, `value`) values ('morningEndTime', '12:00:00');
# 插入晚上开始签到的时间点
insert into automatic_clock_in_config_table(`key`, `value`) values ('eveningStartTime', '17:15:00');
# 插入晚上结束签到的时间点
insert into automatic_clock_in_config_table(`key`, `value`) values ('eveningEndTime', '18:00:00');