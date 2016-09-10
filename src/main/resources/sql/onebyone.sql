DROP TABLE IF EXISTS `OneByOne`;
CREATE TABLE `OneByOne` (
  `bizType` varchar(64) NOT NULL COMMENT '业务类型',
  `bizId` varchar(64) NOT NULL COMMENT '业务ID',
  `method` varchar(64) DEFAULT NULL COMMENT '方法名称',
  `created` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `updated` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`bizType`,`bizId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='一个接一个处理记录表';