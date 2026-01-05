-- liquibase formatted sql
-- changeset unittree:update_db_unittree-3.1.2-4.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE core_admin_right SET icon_url='ti ti-binary-tree' WHERE  id_right='UNITS_MANAGEMENT';