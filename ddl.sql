drop database if exists kickstart;

CREATE DATABASE IF NOT EXISTS kickstart DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS=0;

use kickstart;


DROP TABLE IF EXISTS global_revisions;
CREATE TABLE global_revisions (
  id             int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '版本号',
  operator_id    char(12) DEFAULT NULL COMMENT '操作人id',
  operator_name  varchar(255) DEFAULT NULL COMMENT '操作人姓名',
  revision_time  datetime NOT NULL COMMENT '版本保存时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='全局版本';

DROP TABLE IF EXISTS id_generators;
CREATE TABLE id_generators (
  table_name     varchar(255) NOT NULL COMMENT '表名',
  current_value  bigint(20) DEFAULT NULL COMMENT '当前值',
  formatter      varchar(255) DEFAULT NULL COMMENT '格式',
  PRIMARY KEY (table_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='id自增生成';


SET FOREIGN_KEY_CHECKS=1;