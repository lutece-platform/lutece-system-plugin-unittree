CREATE INDEX index_unittree_unit_assignment ON unittree_unit_assignment (is_active,resource_type,id_assigned_unit);
UPDATE unittree_action SET icon_url='user-x' WHERE id_action=6;