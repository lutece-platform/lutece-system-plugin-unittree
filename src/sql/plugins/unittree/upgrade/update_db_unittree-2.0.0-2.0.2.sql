--liquibase formatted sql
--changeset unittree:update_db_unittree-2.0.0-2.0.2.sql
--preconditions onFail:MARK_RAN onError:WARN
TRUNCATE `unittree_action`;
INSERT INTO `unittree_action` VALUES (1, 'unittree.unit.action.createUnit.name', 'unittree.unit.action.createUnit.description', 'jsp/admin/plugins/unittree/CreateUnit.jsp', 'fa fa-plus fa-fw', 'CREATE', 'unittree.unitAction');
INSERT INTO `unittree_action` VALUES (2, 'unittree.unit.action.modifyUnit.name', 'unittree.unit.action.modifyUnit.description', 'jsp/admin/plugins/unittree/ModifyUnit.jsp', 'fa fa-pencil fa-fw', 'MODIFY', 'unittree.unitAction');
INSERT INTO `unittree_action` VALUES (3, 'unittree.unit.action.deleteUnit.name', 'unittree.unit.action.deleteUnit.description', 'jsp/admin/plugins/unittree/RemoveUnit.jsp', 'fa fa-trash fa-fw', 'DELETE', 'unittree.unitAction');
INSERT INTO `unittree_action` VALUES (4, 'unittree.user.action.modifyUser.name', 'unittree.user.action.modifyUser.description', 'jsp/admin/plugins/unittree/ModifyUser.jsp', 'fa fa-pencil fa-fw', 'MODIFY_USER', 'unittree.unitUserAction');
INSERT INTO `unittree_action` VALUES (5, 'unittree.user.action.moveUser.name', 'unittree.user.action.moveUser.description', 'jsp/admin/plugins/unittree/MoveUser.jsp', 'fa fa-sort fa-fw', 'MOVE_USER', 'unittree.unitUserAction');
INSERT INTO `unittree_action` VALUES (6, 'unittree.user.action.removeUser.name', 'unittree.user.action.removeUser.description', 'jsp/admin/plugins/unittree/RemoveUser.jsp', 'fa fa-user-times fa-fw', 'REMOVE_USER', 'unittree.unitUserAction');
INSERT INTO `unittree_action` VALUES (10, 'unittree.unit.action.moveSubTree.name', 'unittree.unit.action.moveSubTree.description', 'jsp/admin/plugins/unittree/MoveSubTree.jsp', 'fa fa-sort fa-fw', 'MOVE_SUB_TREE', 'unittree.unitAction');