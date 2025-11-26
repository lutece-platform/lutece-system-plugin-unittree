-- liquibase formatted sql
-- changeset unittree:create_db_unittree.sql
-- preconditions onFail:MARK_RAN onError:WARN
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS unittree_unit_user;
DROP TABLE IF EXISTS unittree_unit_assignment;
DROP TABLE IF EXISTS unittree_action;
DROP TABLE IF EXISTS unittree_unit;

SET FOREIGN_KEY_CHECKS = 1;

--
-- Table structure for table form_action
--
CREATE TABLE unittree_unit (
	id_unit INT AUTO_INCREMENT,
	id_parent INT DEFAULT 0 NOT NULL,
	code VARCHAR(255) DEFAULT '' NOT NULL,
	label VARCHAR(255) DEFAULT '' NOT NULL,
	description VARCHAR(255) DEFAULT '' NOT NULL,
	PRIMARY KEY (id_unit)
);

CREATE INDEX index_unittree_unit_code ON unittree_unit (code);

--
-- Table structure for table unittree_unit_user
--
CREATE TABLE unittree_unit_user (
	id_unit INT DEFAULT 0 NOT NULL,
	id_user INT DEFAULT 0 NOT NULL,
	PRIMARY KEY (id_unit, id_user),
	CONSTRAINT fk_id_unit FOREIGN KEY (id_unit)
      REFERENCES unittree_unit (id_unit)
);

--
-- Table structure for table unittree_unit_action
--
CREATE TABLE unittree_action (
	id_action INT DEFAULT 0 NOT NULL,
	name_key VARCHAR(100) DEFAULT '' NOT NULL,
	description_key VARCHAR(100) DEFAULT '' NOT NULL,
	action_url VARCHAR(255) DEFAULT '' NOT NULL,
	icon_url VARCHAR(255) DEFAULT '' NOT NULL,
	action_permission VARCHAR(50) DEFAULT '' NOT NULL,
	action_type VARCHAR(50) DEFAULT '' NOT NULL,
	PRIMARY KEY (id_action)
);

/*==================================================================*/
/* Table structure for table unittree_unit_assignment   */
/*==================================================================*/
CREATE TABLE unittree_unit_assignment (
  id int AUTO_INCREMENT,
  id_resource int NOT NULL,
  resource_type VARCHAR(255) NOT NULL,
  id_assignor_unit int DEFAULT '0' NOT NULL,
  id_assigned_unit int DEFAULT '0' NOT NULL,
  assignment_type VARCHAR(50) NOT NULL,
  assignment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active SMALLINT DEFAULT '0' NOT NULL,
  PRIMARY KEY (id)
 );

CREATE INDEX index_unittree_unit_assignment ON unittree_unit_assignment (is_active,resource_type,id_assigned_unit);
