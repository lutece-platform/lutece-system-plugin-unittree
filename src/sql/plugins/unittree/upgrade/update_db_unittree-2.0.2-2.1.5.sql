DROP TABLE IF EXISTS unittree_unit_assignment;

CREATE TABLE unittree_unit_assignment (
  id int AUTO_INCREMENT,
  id_resource int(11) NOT NULL,
  resource_type VARCHAR(255) NOT NULL,
  id_assignor_unit int(11) NOT NULL DEFAULT '0',
  id_assigned_unit int(11) NOT NULL DEFAULT '0',
  assignment_type VARCHAR(50) NOT NULL,
  assignment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_active SMALLINT NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
 ) ;