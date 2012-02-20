--
-- Table structure for table form_action
--
DROP TABLE IF EXISTS unittree_unit;
CREATE TABLE unittree_unit (
	id_unit INT DEFAULT 0 NOT NULL,
	id_parent INT DEFAULT 0 NOT NULL,
	label VARCHAR(20) DEFAULT NULL,
	description VARCHAR(100) DEFAULT NULL,
	PRIMARY KEY (id_unit)
);
