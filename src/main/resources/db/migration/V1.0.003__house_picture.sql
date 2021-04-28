drop table if exists house_picture;
create table house_picture(
    id int(11) unsigned not null auto_increment comment '图片id',
    house_id int(11) unsigned not null comment '房子id',
    cdn_prefix varchar(64) comment 'CDN前缀',
    width int comment '图片宽度',
    height int comment '图片高度',
    location varchar(128) comment '图片位置',
    path varchar(64) comment '图片路径',
    primary key (id)
)engine = InnoDB comment '房源图片表';


INSERT INTO `house_picture` VALUES (68, 19, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 911, 683, NULL, 'Fp1xPKVYtPsCeVHVQVW0Hif2FXk7');
INSERT INTO `house_picture` VALUES (69, 19, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1012, 683, NULL, 'Fn371N5gLsJvjuIRC4IHjPtMy61h');
INSERT INTO `house_picture` VALUES (70, 24, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1280, 960, NULL, 'Fn1AGNmZfadCIVTJA33gByg6a33B');
INSERT INTO `house_picture` VALUES (71, 24, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FlgoAylUv1ilx1SAtxSyBCJF3bwb');
INSERT INTO `house_picture` VALUES (72, 21, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FnuOFbFtDYTbpPdFoZthR-R0tszC');
INSERT INTO `house_picture` VALUES (73, 21, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 455, 683, NULL, 'FhCiRnyCDQ-O6pXusu5ftmZkIh0-');
INSERT INTO `house_picture` VALUES (74, 20, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FvVqU8LneZZ5xaLBAOM1KXR2Pz1X');
INSERT INTO `house_picture` VALUES (75, 20, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FtNl9uPM6p5PjEs8z2FnOuViNtOM');
INSERT INTO `house_picture` VALUES (76, 18, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1440, 960, NULL, 'FgcD3BufAprERe5y3Gd-Mezu5VAy');
INSERT INTO `house_picture` VALUES (77, 18, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'Fl1lNikhmMIecbTn-JTsurxugtFU');
INSERT INTO `house_picture` VALUES (78, 17, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FvVHtS1qAApFFh6k5LMDm5tliufK');
INSERT INTO `house_picture` VALUES (79, 17, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FpVYJRsLykrBRyUSCEOeqsqWU-bt');
INSERT INTO `house_picture` VALUES (80, 16, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'Fhysh6EcQ_ZTl-jdGe2zaCFi5Uvm');
INSERT INTO `house_picture` VALUES (81, 16, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'Fvb9TDMRtl1haBj9gK9C0k43X0u0');
INSERT INTO `house_picture` VALUES (82, 16, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FvkO1FFyGbrxCP_1O9tA94u2qvbP');
INSERT INTO `house_picture` VALUES (83, 15, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FsxiS6rOTpSg5pK7tv41e8Zpnn_c');
INSERT INTO `house_picture` VALUES (84, 15, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1024, 683, NULL, 'FpOKJ2IEmbA1y1RbIqgZfqFKkJyS');
INSERT INTO `house_picture` VALUES (85, 15, 'http://7xo6gy.com1.z0.glb.clouddn.com/', 1440, 960, NULL, 'Fhxz_c16YmEmIz5UVxrp6ihwbvCk');