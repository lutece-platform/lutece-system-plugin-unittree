-- liquibase formatted sql
-- changeset unittree:update_db_unittree-mariaDB-mySQL-3.1.3-3.1.4.sql dbms:mariadb,mysql 
-- preconditions onFail:MARK_RAN onError:WARN
SET FOREIGN_KEY_CHECKS = 0;

SET SESSION sql_mode='NO_AUTO_VALUE_ON_ZERO';
ALTER TABLE unittree_unit modify COLUMN id_unit int AUTO_INCREMENT;

SET FOREIGN_KEY_CHECKS = 1;