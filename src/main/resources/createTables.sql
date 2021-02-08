# 项目需要的库、表、等结构的sql语句

# 库名
create database if not exists automatic_punch_xust;

# 用户信息表
create table if not exists automatic_punch_user_info_table(
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