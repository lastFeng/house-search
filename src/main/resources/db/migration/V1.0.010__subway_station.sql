use house_search;
drop table if exists subway_station;
create table subway_station(
    id int(11) unsigned not null comment '地铁站id',
    subway_id int(11) unsigned comment '地铁id',
    name varchar(64) comment '地铁站名',
    primary key (id)
) engine = InnoDB comment '地铁站信息表';


INSERT INTO `subway_station` VALUES (5, 1, '上地');
INSERT INTO `subway_station` VALUES (16, 1, '东直门');
INSERT INTO `subway_station` VALUES (4, 1, '五道口');
INSERT INTO `subway_station` VALUES (14, 1, '光熙门');
INSERT INTO `subway_station` VALUES (11, 1, '北苑');
INSERT INTO `subway_station` VALUES (8, 1, '回龙观');
INSERT INTO `subway_station` VALUES (2, 1, '大钟寺');
INSERT INTO `subway_station` VALUES (12, 1, '望京西');
INSERT INTO `subway_station` VALUES (15, 1, '柳芳');
INSERT INTO `subway_station` VALUES (3, 1, '知春路');
INSERT INTO `subway_station` VALUES (10, 1, '立水桥');
INSERT INTO `subway_station` VALUES (13, 1, '芍药居');
INSERT INTO `subway_station` VALUES (6, 1, '西二旗');
INSERT INTO `subway_station` VALUES (1, 1, '西直门');
INSERT INTO `subway_station` VALUES (9, 1, '霍营');
INSERT INTO `subway_station` VALUES (7, 1, '龙泽');
INSERT INTO `subway_station` VALUES (33, 4, '三元家庄');
INSERT INTO `subway_station` VALUES (51, 4, '三元桥');
INSERT INTO `subway_station` VALUES (41, 4, '丰台站');
INSERT INTO `subway_station` VALUES (52, 4, '亮马桥');
INSERT INTO `subway_station` VALUES (27, 4, '健德门');
INSERT INTO `subway_station` VALUES (46, 4, '公主坟');
INSERT INTO `subway_station` VALUES (44, 4, '六里桥');
INSERT INTO `subway_station` VALUES (53, 4, '农业展览馆');
INSERT INTO `subway_station` VALUES (62, 4, '分钟寺');
INSERT INTO `subway_station` VALUES (59, 4, '劲松');
INSERT INTO `subway_station` VALUES (28, 4, '北土城');
INSERT INTO `subway_station` VALUES (61, 4, '十里河');
INSERT INTO `subway_station` VALUES (58, 4, '双井');
INSERT INTO `subway_station` VALUES (55, 4, '呼家楼');
INSERT INTO `subway_station` VALUES (54, 4, '团结湖');
INSERT INTO `subway_station` VALUES (57, 4, '国贸');
INSERT INTO `subway_station` VALUES (35, 4, '大红门');
INSERT INTO `subway_station` VALUES (32, 4, '太阳宫');
INSERT INTO `subway_station` VALUES (29, 4, '安贞门');
INSERT INTO `subway_station` VALUES (64, 4, '宋家庄');
INSERT INTO `subway_station` VALUES (20, 4, '巴沟');
INSERT INTO `subway_station` VALUES (30, 4, '惠新西街南口');
INSERT INTO `subway_station` VALUES (48, 4, '慈寿寺');
INSERT INTO `subway_station` VALUES (63, 4, '成寿寺');
INSERT INTO `subway_station` VALUES (42, 4, '泥洼');
INSERT INTO `subway_station` VALUES (22, 4, '海淀黄庄');
INSERT INTO `subway_station` VALUES (60, 4, '潘家园');
INSERT INTO `subway_station` VALUES (19, 4, '火器营');
INSERT INTO `subway_station` VALUES (26, 4, '牡丹园');
INSERT INTO `subway_station` VALUES (24, 4, '知春路');
INSERT INTO `subway_station` VALUES (23, 4, '知春里');
INSERT INTO `subway_station` VALUES (34, 4, '石榴庄');
INSERT INTO `subway_station` VALUES (39, 4, '纪家庙');
INSERT INTO `subway_station` VALUES (31, 4, '芍药居');
INSERT INTO `subway_station` VALUES (21, 4, '苏州街');
INSERT INTO `subway_station` VALUES (38, 4, '草桥');
INSERT INTO `subway_station` VALUES (45, 4, '莲花桥');
INSERT INTO `subway_station` VALUES (25, 4, '西土城');
INSERT INTO `subway_station` VALUES (43, 4, '西局');
INSERT INTO `subway_station` VALUES (47, 4, '西钓鱼台');
INSERT INTO `subway_station` VALUES (36, 4, '角门东');
INSERT INTO `subway_station` VALUES (37, 4, '角门西');
INSERT INTO `subway_station` VALUES (17, 4, '车道沟');
INSERT INTO `subway_station` VALUES (56, 4, '金台夕照');
INSERT INTO `subway_station` VALUES (18, 4, '长春桥');
INSERT INTO `subway_station` VALUES (40, 4, '首经贸');