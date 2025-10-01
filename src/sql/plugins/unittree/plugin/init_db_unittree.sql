-- liquibase formatted sql
-- changeset unittree:init_db_unittree.sql
-- preconditions onFail:MARK_RAN onError:WARN

--
-- Dumping data for table unittree_unit
--
INSERT INTO unittree_unit (id_unit, id_parent, code, label, description) VALUES (0,-1,'ROOT', 'Racine','Racine des entités');

--
-- Dumping data for table unittree_unit_action
--
INSERT INTO unittree_action VALUES (1, 'unittree.unit.action.createUnit.name', 'unittree.unit.action.createUnit.description', 'jsp/admin/plugins/unittree/CreateUnit.jsp', 'plus', 'CREATE', 'unittree.unitAction');
INSERT INTO unittree_action VALUES (2, 'unittree.unit.action.modifyUnit.name', 'unittree.unit.action.modifyUnit.description', 'jsp/admin/plugins/unittree/ModifyUnit.jsp', 'edit', 'MODIFY', 'unittree.unitAction');
INSERT INTO unittree_action VALUES (3, 'unittree.unit.action.deleteUnit.name', 'unittree.unit.action.deleteUnit.description', 'jsp/admin/plugins/unittree/RemoveUnit.jsp', 'trash', 'DELETE', 'unittree.unitAction');
INSERT INTO unittree_action VALUES (5, 'unittree.user.action.moveUser.name', 'unittree.user.action.moveUser.description', 'jsp/admin/plugins/unittree/MoveUser.jsp', 'sort', 'MOVE_USER', 'unittree.unitUserAction');
INSERT INTO unittree_action VALUES (6, 'unittree.user.action.removeUser.name', 'unittree.user.action.removeUser.description', 'jsp/admin/plugins/unittree/RemoveUser.jsp', 'user-x', 'REMOVE_USER', 'unittree.unitUserAction');
INSERT INTO unittree_action VALUES (10, 'unittree.unit.action.moveSubTree.name', 'unittree.unit.action.moveSubTree.description', 'jsp/admin/plugins/unittree/MoveSubTree.jsp', 'sort', 'MOVE_SUB_TREE', 'unittree.unitAction');