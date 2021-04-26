drop table if exists `user`;
create table `user`(
    id int(11) unsigned not null comment '用户id',
    name varchar(64) comment '用户名称',
    email varchar(64) comment '用户邮箱',
    phone_number varchar(20) comment '用户手机号',
    password varchar(128) comment '用户密码',
    status int default 0 comment '当前状态',
    create_time datetime default now() comment '创建时间',
    last_login_time datetime default now() comment '最后登录时间',
    last_update_time datetime default now() comment '最后更新时间',
    avatar varchar(128) comment '用户图片',
    primary key (id)
)engine=InnoDB comment '用户表';

INSERT INTO `user` VALUES (1, 'waliwali', 'wali@wali.com', '15111111111', '6fd1aad88b038aeecd9adeccc92b0bd1', 1, '2017-08-25 15:18:20', '2017-08-25 12:00:00', '2017-11-26 10:29:02', 'http://7xo6gy.com1.z0.glb.clouddn.com/99ff568bd61c744bf31185aeddf13580.png');
INSERT INTO `user` VALUES (2, 'admin', 'admin@imooc.com', '1388888888', '$2a$10$6xFSOLZ3HWnjUhFq4S3wHeOsdOiY7oMGQclpovZaC/X6oHT/vWdIC', 1, '2017-08-27 09:07:05', '2017-08-27 09:07:07', '2019-06-22 13:11:47', 'http://7xo6gy.com1.z0.glb.clouddn.com/99ff568bd61c744bf31185aeddf13580.png');
INSERT INTO `user` VALUES (5, '138****8888', NULL, '13888888888', NULL, 0, '2017-11-25 17:56:45', '2017-11-25 17:56:45', '2017-11-25 17:56:45', NULL);
INSERT INTO `user` VALUES (8, '151****9677', NULL, '15110059677', NULL, 0, '2017-11-25 18:58:18', '2017-11-25 18:58:18', '2017-11-25 18:58:18', NULL);