use house_search;
drop table if exists subway;
create table subway(
    id int(11) unsigned not null comment '地铁id',
    name varchar(64) comment '地铁名',
    city_en_name varchar(64) comment '城市名',
    primary key (id)
) engine = InnoDB comment '地铁信息表';

INSERT INTO `subway` VALUES (1, '13号线', 'bj');
INSERT INTO `subway` VALUES (2, '1号线', 'bj');
INSERT INTO `subway` VALUES (3, '2号线', 'bj');
INSERT INTO `subway` VALUES (4, '10号线', 'bj');
INSERT INTO `subway` VALUES (5, '8号线', 'bj');
INSERT INTO `subway` VALUES (6, '9号线', 'bj');
INSERT INTO `subway` VALUES (7, '7号线', 'bj');