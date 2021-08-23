UPDATE unittree_action SET icon_url='plus' WHERE id_action=1;
UPDATE unittree_action SET icon_url='edit' WHERE id_action=2;
UPDATE unittree_action SET icon_url='trash' WHERE id_action=3;
UPDATE unittree_action SET icon_url='sort' WHERE id_action=5;
UPDATE unittree_action SET icon_url='user-times' WHERE id_action=6;
UPDATE unittree_action SET icon_url='sort' WHERE id_action=10;

DELETE FROM unittree_action WHERE id_action = 4;
