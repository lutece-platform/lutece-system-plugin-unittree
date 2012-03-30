--
-- UNITTREE-
--
DROP TABLE IF EXISTS unittree_sector CASCADE;
DROP TABLE IF EXISTS unittree_unit_sector CASCADE;

ALTER TABLE unittree_unit_user ADD CONSTRAINT fk_id_unit FOREIGN KEY (id_unit) REFERENCES unittree_unit(id_unit);
