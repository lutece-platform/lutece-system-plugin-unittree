DROP TABLE IF EXISTS unittree_unit_assignment;

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