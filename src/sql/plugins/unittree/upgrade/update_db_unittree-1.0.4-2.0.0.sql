-- liquibase formatted sql
-- changeset unittree:update_db_unittree-1.0.4-2.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE unittree_action SET icon_url = 'icon-plus' WHERE id_action = 1;
UPDATE unittree_action SET icon_url = 'icon-edit' WHERE id_action = 2;
UPDATE unittree_action SET icon_url = 'icon-trash' WHERE id_action = 3;
UPDATE unittree_action SET icon_url = 'icon-edit' WHERE id_action = 4;
UPDATE unittree_action SET icon_url = 'icon-share' WHERE id_action = 5;
UPDATE unittree_action SET icon_url = 'icon-trash' WHERE id_action = 6;
UPDATE unittree_action SET icon_url = 'icon-share-alt' WHERE id_action = 10;
