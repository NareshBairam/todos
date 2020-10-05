CREATE TABLE todo (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;


CREATE TABLE tasks (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  todo_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (todo_id) REFERENCES todo(id)
);