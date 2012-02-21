--
-- Dumping data for table unittree_unit
--
INSERT INTO unittree_unit (id_unit, id_parent, label, description) VALUES (0,-1,'Racine','Racine des entités');

--
-- Dumping data for table unittree_unit_action
--
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,resource_type) VALUES 
(1,'unittree.unit.action.createUnit.name','unittree.unit.action.createUnit.description','jsp/admin/plugins/unittree/CreateUnit.jsp','images/admin/skin/plugins/unittree/actions/create_unit.png','CREATE', 'UNIT_TYPE');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,resource_type) VALUES 
(2,'unittree.unit.action.modifyUnit.name','unittree.unit.action.modifyUnit.description','jsp/admin/plugins/unittree/ModifyUnit.jsp','images/admin/skin/plugins/unittree/actions/modify_unit.png','MODIFY', 'UNIT_TYPE');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,resource_type) VALUES 
(3,'unittree.unit.action.deleteUnit.name','unittree.unit.action.deleteUnit.description','jsp/admin/plugins/unittree/RemoveUnit.jsp','images/admin/skin/plugins/unittree/actions/delete_unit.png','DELETE', 'UNIT_TYPE');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,resource_type) VALUES 
(4,'unittree.user.action.addUser.name','unittree.user.action.addUser.description','jsp/admin/plugins/unittree/AddUser.jsp','images/admin/skin/plugins/unittree/actions/add_user.png','ADD_USER', 'UNIT_USER_TYPE');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,resource_type) VALUES 
(5,'unittree.user.action.modifyUser.name','unittree.user.action.modifyUser.description','jsp/admin/plugins/unittree/ModifyUser.jsp','images/admin/skin/plugins/unittree/actions/modify_user.png','MODIFY_USER', 'UNIT_USER_TYPE');
INSERT INTO unittree_action (id_action,name_key,description_key,action_url,icon_url,action_permission,resource_type) VALUES 
(6,'unittree.user.action.removeUser.name','unittree.user.action.removeUser.description','jsp/admin/plugins/unittree/RemoveUser.jsp','images/admin/skin/plugins/unittree/actions/remove_user.png','REMOVE_USER', 'UNIT_USER_TYPE');
