
--
-- Dumping data for table unittree_unit
--
INSERT INTO unittree_unit (id_unit, id_parent, label, description) VALUES (0,-1,'Racine','Racine des entités');

--
-- Dumping data for table unittree_unit_action
--
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(1,'unittree.unit.action.createUnit.name','unittree.unit.action.createUnit.description','jsp/admin/plugins/unittree/CreateUnit.jsp','icon-plus','CREATE', 'unittree.unitAction');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(2,'unittree.unit.action.modifyUnit.name','unittree.unit.action.modifyUnit.description','jsp/admin/plugins/unittree/ModifyUnit.jsp','icon-edit','MODIFY', 'unittree.unitAction');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(3,'unittree.unit.action.deleteUnit.name','unittree.unit.action.deleteUnit.description','jsp/admin/plugins/unittree/RemoveUnit.jsp','icon-trash','DELETE', 'unittree.unitAction');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(4,'unittree.user.action.modifyUser.name','unittree.user.action.modifyUser.description','jsp/admin/plugins/unittree/ModifyUser.jsp','icon-edit','MODIFY_USER', 'unittree.unitUserAction');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(5,'unittree.user.action.moveUser.name','unittree.user.action.moveUser.description','jsp/admin/plugins/unittree/MoveUser.jsp','icon-share','MOVE_USER', 'unittree.unitUserAction');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(6,'unittree.user.action.removeUser.name','unittree.user.action.removeUser.description','jsp/admin/plugins/unittree/RemoveUser.jsp','icon-trash','REMOVE_USER', 'unittree.unitUserAction');

INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,action_type) VALUES 
(10,'unittree.unit.action.moveSubTree.name','unittree.unit.action.moveSubTree.description','jsp/admin/plugins/unittree/MoveSubTree.jsp','icon-share-alt','MOVE_SUB_TREE', 'unittree.unitAction');
