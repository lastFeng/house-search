use house_search;
drop table if exists house_tag;
create table house_tag(
    house_id int(11) unsigned comment '房源id',
    id int(11) unsigned not null comment '标签id',
    name varchar(64) comment '标签名',
    primary key (id)
) engine = InnoDB comment '房源标签';

INSERT INTO `house_tag` VALUES (15, 18, '独立阳台');
INSERT INTO `house_tag` VALUES (15, 17, '空调');
INSERT INTO `house_tag` VALUES (16, 16, '光照充足');
INSERT INTO `house_tag` VALUES (17, 15, '随时看房');
INSERT INTO `house_tag` VALUES (17, 14, '集体供暖');
INSERT INTO `house_tag` VALUES (18, 13, '精装修');
INSERT INTO `house_tag` VALUES (19, 12, '独立卫生间');
INSERT INTO `house_tag` VALUES (19, 11, '独立阳台');
INSERT INTO `house_tag` VALUES (21, 19, '光照充足');
INSERT INTO `house_tag` VALUES (21, 20, '独立卫生间');
INSERT INTO `house_tag` VALUES (24, 10, '光照充足');
INSERT INTO `house_tag` VALUES (24, 3, '精装修');
INSERT INTO `house_tag` VALUES (24, 8, '集体供暖');
INSERT INTO `house_tag` VALUES (25, 21, '独立阳台');