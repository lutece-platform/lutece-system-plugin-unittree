-- liquibase formatted sql
-- changeset unittree:update_db_unittree-3.1.1-3.1.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
CREATE INDEX index_unittree_unit_assignment ON unittree_unit_assignment (is_active,resource_type,id_assigned_unit);
UPDATE unittree_action SET icon_url='user-x' WHERE id_action=6;