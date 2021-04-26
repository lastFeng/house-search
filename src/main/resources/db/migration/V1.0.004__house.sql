drop table if exists house;
create table house(
    id int(11) unsigned not null comment '房子id',
    title varchar(64) comment '房子标题',
    price int default 0 comment '房价',
    area int default 10 comment '房子面积',
    room int default 1 comment '房间号',
    floor int default 1 comment '楼层',
    total_floor int default 1 comment '总楼层',
    watch_times int default 0 comment '看楼次数',
    build_year int default 1993 comment '建立年份',
    status int default 1 comment '状态',
    create_time datetime default now() comment '创建时间',
    last_update_time datetime default now() comment '最后更新时间',
    city_en_name varchar(64) comment '城市名',
    region_en_name varchar(64) comment '区域名',
    cover varchar(64) comment '封面',
    direction int default 0 comment '房屋朝向',
    distance_to_subway int default -1 comment '邻近地铁，默认-1为无地铁',
    parlour int default 0 comment '客厅数目',
    district varchar(64) comment '小区',
    admin_id int unsigned comment '管理员id',
    bathroom int default 0 comment '浴室数',
    street varchar(64) comment '街道',
    primary key (id)
)engine = InnoDB comment '房源表';

INSERT INTO `house` VALUES (15, '富力城 国贸CBD 时尚休闲 商务办公', 6200, 70, 2, 10, 20, 2, 2005, 1, '2017-09-06 18:56:14', '2017-12-03 11:13:46', 'bj', 'hdq', 'Fhxz_c16YmEmIz5UVxrp6ihwbvCk', 2, 10, 1, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (16, '富力城 国贸CBD 时尚休闲 商务办公', 6300, 70, 2, 10, 20, 0, 2012, 1, '2017-09-06 19:53:35', '2017-12-03 11:13:42', 'bj', 'hdq', 'FvkO1FFyGbrxCP_1O9tA94u2qvbP', 1, -1, 1, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (17, '二环东直门地铁站附近、王府井、天安门、国贸、三里屯、南锣鼓巷', 3000, 35, 1, 5, 10, 2, 2013, 1, '2017-09-06 20:45:35', '2017-12-03 11:13:36', 'bj', 'hdq', 'FpVYJRsLykrBRyUSCEOeqsqWU-bt', 1, 200, 0, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (18, '华贸城 东向一居挑空loft 干净温馨 随时可以签约', 5700, 52, 1, 7, 20, 0, 2012, 1, '2017-09-06 21:01:02', '2017-12-03 11:13:30', 'bj', 'hdq', 'Fl1lNikhmMIecbTn-JTsurxugtFU', 2, 1085, 1, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (19, '望春园板楼三居室 自住精装 南北通透 采光好视野棒！', 9200, 132, 3, 6, 14, 0, 2005, 1, '2017-09-06 22:44:25', '2017-12-03 11:13:25', 'bj', 'hdq', 'Fp1xPKVYtPsCeVHVQVW0Hif2FXk7', 2, 1108, 2, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (20, '高大上的整租两居室 业主诚意出租', 5400, 56, 2, 12, 20, 0, 2012, 1, '2017-09-06 23:39:50', '2017-12-03 11:13:20', 'bj', 'hdq', 'FvVqU8LneZZ5xaLBAOM1KXR2Pz1X', 2, -1, 1, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (21, '新康园 正规三居室 精装修 家电家具齐全', 1900, 18, 1, 13, 25, 0, 2012, 1, '2017-09-07 00:52:47', '2017-12-03 11:13:15', 'bj', 'hdq', 'FnuOFbFtDYTbpPdFoZthR-R0tszC', 3, 1302, 0, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (24, '湖光壹号望京华府183-387㎡阔景大宅', 50000, 288, 5, 1, 1, 0, 2015, 1, '2017-09-07 11:42:20', '2017-12-03 11:13:10', 'bj', 'hdq', 'FvVqU8LneZZ5xaLBAOM1KXR2Pz1X', 5, 200, 3, '融泽嘉园', 2, 0, '龙域西二路');
INSERT INTO `house` VALUES (25, '测试房源-编辑', 3000, 59, 2, 10, 20, 0, 2010, 3, '2017-10-28 22:34:48', '2017-11-11 12:22:50', 'bj', 'cpq', 'FtbxR2LY98lnnX_TPOgOPzti3k7G', 2, 1000, 1, '融泽嘉园', 2, 0, '龙域中街');
