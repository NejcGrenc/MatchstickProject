
DROP TABLE IF EXISTS test_entity;
CREATE TABLE test_entity (
	id  	SERIAL PRIMARY KEY,
	name	varchar(10),
	value	bigint
);

DROP TABLE IF EXISTS test_equation;
CREATE TABLE test_equation (
	id  		SERIAL PRIMARY KEY,
	equation	varchar(7) NOT NULL
);
