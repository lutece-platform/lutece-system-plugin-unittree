-- liquibase formatted sql
-- changeset unittree:update_db_unittree-4.0.1-4.1.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment the action "move the unit" is bound to the permission MOVE_UNIT the move page checks, MOVE_SUB_TREE is not a permission of the resource type UNIT_TYPE
UPDATE unittree_action SET action_permission = 'MOVE_UNIT' WHERE id_action = 10 AND action_permission = 'MOVE_SUB_TREE';
