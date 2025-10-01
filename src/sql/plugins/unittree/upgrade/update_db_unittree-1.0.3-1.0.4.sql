-- liquibase formatted sql
-- changeset unittree:update_db_unittree-1.0.3-1.0.4.sql
-- preconditions onFail:MARK_RAN onError:WARN
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(10,'unittree.unit.action.moveSubTree.name','unittree.unit.action.moveSubTree.description','jsp/admin/plugins/unittree/MoveSubTree.jsp','images/admin/skin/plugins/unittree/actions/move_user.png','MOVE_SUB_TREE', 'unittree.unitAction');
