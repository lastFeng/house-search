use house_search;
drop table if exists support_address;
create table support_address(
    id int(11) unsigned not null auto_increment comment 'id',
    belong_to varchar(64) comment '所属城市',
    en_name varchar(64) comment '英文名称',
    cn_name varchar(128) comment '中文名称',
    level varchar(64) comment '级别',
    baidu_map_lng double comment '百度地图经度',
    baidu_map_lat double comment '百度地图纬度',
    primary key (id)
)engine = InnoDB comment '支持城市表';

INSERT INTO `support_address` VALUES (4, 'bj', 'bj', '北京', 'city', 116.395645, 39.929986);
INSERT INTO `support_address` VALUES (5, 'sh', 'sh', '上海', 'city', 121.487899, 31.249162);
INSERT INTO `support_address` VALUES (6, 'hb', 'sjz', '石家庄', 'city', 114.522082, 38.048958);
INSERT INTO `support_address` VALUES (7, 'hb', 'ts', '唐山', 'city', 118.183451, 39.650531);
INSERT INTO `support_address` VALUES (8, 'hb', 'hd', '邯郸', 'city', 114.482694, 36.609308);
INSERT INTO `support_address` VALUES (9, 'bj', 'dcq', '东城区', 'region', 116.42188470126446, 39.93857401298612);
INSERT INTO `support_address` VALUES (10, 'bj', 'xcq', '西城区', 'region', 116.37319010401802, 39.93428014370851);
INSERT INTO `support_address` VALUES (12, 'bj', 'hdq', '海淀区', 'region', 116.23967780102151, 40.03316204507791);
INSERT INTO `support_address` VALUES (13, 'bj', 'cpq', '昌平区', 'region', 116.21645635689414, 40.2217235498323);
INSERT INTO `support_address` VALUES (14, 'sh', 'ptq', '普陀区', 'region', 121.39844294374956, 31.263742929075534);
INSERT INTO `support_address` VALUES (15, 'sjz', 'caq', '长安区', 'region', 114.59262155387033, 38.07687479578663);
INSERT INTO `support_address` VALUES (16, 'sjz', 'qdq', '桥东区', 'region', 114.51078430496142, 38.06338975380927);
INSERT INTO `support_address` VALUES (17, 'sjz', 'qxq', '桥西区', 'region', 114.43813995531943, 38.033364550068136);
INSERT INTO `support_address` VALUES (18, 'sjz', 'xhq', '新华区', 'region', 114.4535014286928, 38.117218640478164);
INSERT INTO `support_address` VALUES (19, 'bj', 'cyq', '朝阳区', 'region', 116.52169489108084, 39.95895316640668);