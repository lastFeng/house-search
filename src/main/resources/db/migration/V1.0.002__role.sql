drop table if exists `role`;
create table `role`(
    id int(11) unsigned not null comment '角色id',
    user_id int(11) unsigned not null comment '用户id',
    name varchar(64) comment '角色名称',
    primary key (id)
)engine = InnoDB comment '角色表';

INSERT INTO `role` VALUES (1, 1, 'USER');
INSERT INTO `role` VALUES (2, 2, 'ADMIN');
INSERT INTO `role` VALUES (3, 3, 'USER');
INSERT INTO `role` VALUES (4, 4, 'USER');
INSERT INTO `role` VALUES (5, 5, 'USER');
INSERT INTO `role` VALUES (6, 6, 'USER');
INSERT INTO `role` VALUES (7, 7, 'USER');
INSERT INTO `role` VALUES (8, 8, 'USER');