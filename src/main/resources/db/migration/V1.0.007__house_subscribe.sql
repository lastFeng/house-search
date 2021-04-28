use house_search;
drop table if exists house_subscribe;
create table house_subscribe(
    id int(11) unsigned not null auto_increment comment '订阅id',
    house_id int(11) unsigned comment '房源id',
    user_id int(11) unsigned comment '用户id',
    `desc` varchar(64) comment '用户描述',
    status int default 0 comment '房源状态',
    create_time datetime default now() comment '创建时间',
    last_update_time datetime default now() comment '最后更新时间',
    order_time datetime default now() comment '预定时间',
    telephone varchar(32) comment '预留手机号',
    admin_id int(11) unsigned comment '管理员id',
    primary key (id)
) engine = InnoDB comment '房源订阅表';

INSERT INTO `house_subscribe` VALUES (9, 17, 1, NULL, 3, '2017-11-26 11:06:23', '2017-12-02 09:21:01', '2017-12-03 00:00:00', '13888888888', 2);