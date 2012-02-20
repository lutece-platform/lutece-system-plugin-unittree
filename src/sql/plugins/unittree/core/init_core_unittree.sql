--
-- Dumping data for table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url) VALUES 
('UNITS_MANAGEMENT','unittree.adminFeature.unitsManagement.name',0,'jsp/admin/plugins/unittree/ManageUnits.jsp','unittree.adminFeature.unitsManagement.description',0,'unittree','MANAGERS','images/admin/skin/plugins/unittree/unittree.png','');

--
-- Dumping data for table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('UNITS_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('UNITS_MANAGEMENT',2);
