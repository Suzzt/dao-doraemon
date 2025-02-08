CREATE TABLE `sys_user_info`
(
    `id`        bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_name` varchar(64)   DEFAULT NULL COMMENT '昵称，最多64个字符',
    `account`   varchar(32)  NOT NULL COMMENT '账户名，2-20个字符',
    `password`  varchar(256) NOT NULL COMMENT '密码，通过AES对称加密',
    `avatar`    varchar(1024) DEFAULT NULL COMMENT '头像地址',
    `create_at` datetime     NOT NULL COMMENT '注册时间',
    `version`   int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    `update_at` datetime     NOT NULL COMMENT '更新时间',
    `status`    int(11) NOT NULL DEFAULT 0 COMMENT '0：正常，-1：删除，1：禁用',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统-用户信息';