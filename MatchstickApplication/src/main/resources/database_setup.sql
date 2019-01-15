
DROP TABLE IF EXISTS session;
CREATE TABLE session (
	id  		SERIAL PRIMARY KEY,
	tag  		varchar(40) NOT NULL,
	risk		integer,
	lang		varchar(20),
	subject_id	integer,
	test_tasks_only boolean,
	snoop_enabled   boolean
);

DROP TABLE IF EXISTS subject;
CREATE TABLE subject (
	id  			SERIAL PRIMARY KEY,
	name  			varchar(40) NOT NULL,
	complete_data	boolean,
	age				integer,
	sex				varchar(8),
	language		varchar(20),
	password		varchar(40)
);

DROP TABLE IF EXISTS task_session;
CREATE TABLE task_session (
	id  				SERIAL PRIMARY KEY,
	session_id			integer NOT NULL,
	task_type			varchar(40),
	start_time			bigint,
	matchstick_group	varchar(8),
	complete			boolean,
	notes				varchar(500)		-- Larger notes are cut off
);

DROP TABLE IF EXISTS matchstick_task;
CREATE TABLE matchstick_task (
	id  				SERIAL PRIMARY KEY,
	task_session_id		integer NOT NULL,
	number				integer,
	status				varchar(10),
	original_eq	 		varchar(12),
	solved_eq	 		varchar(12),
	time	 			bigint,
	moves	 			int,
	transfer	 		real
);

DROP TABLE IF EXISTS images_task;
CREATE TABLE images_task (
	id  				SERIAL PRIMARY KEY,
	task_session_id		integer NOT NULL,
	number				integer,
	image_id	 		varchar(12),
	time	 			bigint,
	correct	 			boolean
);

DROP TABLE IF EXISTS matchstick_action;
CREATE TABLE matchstick_action (
	id  					SERIAL PRIMARY KEY,
	matchstick_task_id		integer NOT NULL,
	type					varchar(10),
	start_eq				varchar(10),
	end_eq					varchar(10),
	start_matchstick_loc	varchar(100),
	end_matchstick_loc		varchar(100),
	start_time				bigint,
	end_time				bigint
);