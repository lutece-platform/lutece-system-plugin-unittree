--
-- UNITTREE-2 : Init the database with samples
--
ALTER TABLE unittree_unit MODIFY label VARCHAR(255) DEFAULT '' NOT NULL;
ALTER TABLE unittree_unit MODIFY description VARCHAR(255) DEFAULT '' NOT NULL;
ALTER TABLE unittree_sector MODIFY name VARCHAR(255) DEFAULT '' NOT NULL;
