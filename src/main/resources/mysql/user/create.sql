CREATE TABLE user (username VARCHAR(16) UNIQUE KEY NOT NULL, pw BLOB) ENGINE = INNODB;
INSERT INTO version (relation, version) VALUES ('user', '1.0.0');