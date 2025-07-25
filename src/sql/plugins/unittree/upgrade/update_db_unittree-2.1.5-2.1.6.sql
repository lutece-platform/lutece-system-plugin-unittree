--liquibase formatted sql
--changeset unittree:update_db_unittree-2.1.5-2.1.6.sql
--preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE unittree_unit ADD COLUMN code VARCHAR(255) DEFAULT '' NOT NULL;