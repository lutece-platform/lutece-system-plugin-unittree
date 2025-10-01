-- liquibase formatted sql
-- changeset unittree:update_db_unittree-1.0.1-1.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
--
-- UNITTREE-
--
DROP TABLE IF EXISTS unittree_sector CASCADE;
DROP TABLE IF EXISTS unittree_unit_sector CASCADE;

ALTER TABLE unittree_unit_user ADD CONSTRAINT fk_id_unit FOREIGN KEY (id_unit) REFERENCES unittree_unit(id_unit);
