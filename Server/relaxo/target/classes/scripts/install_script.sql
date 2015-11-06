DROP TABLE IF EXISTS task_component_type CASCADE;
DROP TABLE IF EXISTS workflow_item_type CASCADE;
DROP TABLE IF EXISTS component_type CASCADE;
DROP TABLE IF EXISTS workflow_item_type CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS usergroup CASCADE;
DROP TABLE IF EXISTS token CASCADE;
DROP TABLE IF EXISTS user_usergroup CASCADE;
DROP TABLE IF EXISTS workflow CASCADE;
DROP TABLE IF EXISTS workflow_item CASCADE;
DROP TABLE IF EXISTS workflow_item_script CASCADE;
DROP TABLE IF EXISTS next_workflow_item CASCADE;
DROP TABLE IF EXISTS workflow_item_usergroup CASCADE;
DROP TABLE IF EXISTS workflow_item_task_component CASCADE;
DROP TABLE IF EXISTS job CASCADE;
DROP TABLE IF EXISTS job_workflow_item CASCADE;
DROP TABLE IF EXISTS job_task CASCADE;
DROP TABLE IF EXISTS join_branch CASCADE;
DROP TABLE IF EXISTS task_component CASCADE;
DROP TABLE IF EXISTS task_component_int CASCADE;
DROP TABLE IF EXISTS task_component_float CASCADE;
DROP TABLE IF EXISTS task_component_text CASCADE;
DROP TABLE IF EXISTS task_component_date CASCADE;
DROP TABLE IF EXISTS job_task_component CASCADE;
DROP TABLE IF EXISTS job_task_component_int CASCADE;
DROP TABLE IF EXISTS job_task_component_float CASCADE;
DROP TABLE IF EXISTS job_task_component_text CASCADE;
DROP TABLE IF EXISTS job_task_component_textarea CASCADE;
DROP TABLE IF EXISTS job_task_component_date CASCADE;
DROP TABLE IF EXISTS form_group CASCADE;
DROP TABLE IF EXISTS workflow_item_decision CASCADE;
DROP TABLE IF EXISTS task_variables CASCADE;


-- ##### MISC #####
-- Relation Task Component Type
CREATE TABLE task_component_type (
	name VARCHAR(20),
	PRIMARY KEY(name)
);

CREATE TABLE workflow_item_type (
	name VARCHAR(20),
	PRIMARY KEY(name)
);

-- Relation Workflow Item Type

-- ##### USERMANAGEMENT #####
-- Relation User
CREATE TABLE users (
	id SERIAL,
	username VARCHAR(80) UNIQUE,
	password VARCHAR(80),
	prename VARCHAR(80),
	name VARCHAR(80),
	admin BOOLEAN,
	active BOOLEAN DEFAULT TRUE,
	PRIMARY KEY(id)
);

-- Relation Usergroup
CREATE TABLE usergroup (
	id SERIAL,
	name VARCHAR(80) UNIQUE NOT NULL,
	PRIMARY KEY(id)
);

-- Relation Session Token
CREATE TABLE token (
	token VARCHAR(32),
	user_id INT UNIQUE,
	PRIMARY KEY(token),
	FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Relation User in Usergroups
CREATE TABLE user_usergroup (
	user_id INT,
	usergroup_id INT,
	PRIMARY KEY(user_id, usergroup_id),
	FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY(usergroup_id) REFERENCES usergroup(id)
);


-- ##### WORKFLOW #####
-- Relation Workflow
CREATE TABLE workflow (
	id SERIAL,
	name VARCHAR(80),
	creator_id INT,
	start_item_id INT,
	creation_date TIMESTAMP,
	runnable BOOLEAN DEFAULT false,
	PRIMARY KEY(id),
	FOREIGN KEY(creator_id) REFERENCES users(id)
);

-- Relation Task Component Form Groups
CREATE TABLE form_group (
	id SERIAL,
	name VARCHAR(80),
	workflow_id INT,
	PRIMARY KEY (id),
	FOREIGN KEY (workflow_id) REFERENCES workflow(id) ON DELETE CASCADE
);

-- Relation Workflow Item
CREATE TABLE workflow_item (
	id SERIAL,
	workflow_id INT,
	type VARCHAR(20),
	name VARCHAR(80),
	comment TEXT,
	x_pos INT,
	y_pos INT,
	locked BOOLEAN DEFAULT FALSE,
	PRIMARY KEY(id),
	FOREIGN KEY(type) REFERENCES workflow_item_type(name),
	FOREIGN KEY(workflow_id) REFERENCES workflow(id) ON DELETE CASCADE
);

-- Relation Next Workflow Item(Workflow Item hat ein oder mehrere nächste Workflow Items)
CREATE TABLE next_workflow_item (
	workflow_item_id INT,
	next_workflow_item_id INT,
	PRIMARY KEY(workflow_item_id, next_workflow_item_id),
	FOREIGN KEY(workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(next_workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE
);

-- Relation Berechtigung Usergroup auf Workflow Item
CREATE TABLE workflow_item_usergroup (
	workflow_item_id INT,
	usergroup_id INT,
	PRIMARY KEY(workflow_item_id, usergroup_id),
	FOREIGN KEY(workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(usergroup_id) REFERENCES usergroup(id)
);

-- ##### JOB #####
-- Relation Job
CREATE TABLE job (
	id SERIAL,
	workflow_id INT,
	creator_id INT,
	start_time TIMESTAMP,
	end_time TIMESTAMP,
	PRIMARY KEY(id),
	FOREIGN KEY(workflow_id) REFERENCES workflow(id) ON DELETE CASCADE,
	FOREIGN KEY(creator_id) REFERENCES users(id)
);

-- Relation Job Workflow Item
CREATE TABLE job_workflow_item (
	id SERIAL,
	workflow_item_id INT,
	job_id INT,
	done BOOLEAN DEFAULT FALSE,
	PRIMARY KEY(id),
	FOREIGN KEY(workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(job_id) REFERENCES job(id) ON DELETE CASCADE
);


-- Relation Job Task (ist ein Job Workflow Item)
CREATE TABLE job_task (
	job_workflow_item_id INT,
	start_time TIMESTAMP,
	end_time TIMESTAMP,
	blocker_user_id INT,
	editor_user_id INT,
	PRIMARY KEY(job_workflow_item_id),
	FOREIGN KEY(job_workflow_item_id) REFERENCES job_workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(blocker_user_id) REFERENCES users(id),
	FOREIGN KEY(editor_user_id) REFERENCES users(id)
);

-- ##### Task Component #####
-- Relation: Task Component
CREATE TABLE task_component (
	id SERIAL,
	name VARCHAR(80),
	comment TEXT,
	index int,
	required BOOLEAN,
	type VARCHAR(20),
	form_group_id INT,
	PRIMARY KEY(id),
	FOREIGN KEY(type) REFERENCES task_component_type(name),
	FOREIGN KEY (form_group_id) REFERENCES form_group(id) ON DELETE CASCADE
);

-- Relation: Task Component: Date
CREATE TABLE task_component_date (
	task_component_id INT,
	default_value TIMESTAMP,
	PRIMARY KEY(task_component_id),
	FOREIGN KEY(task_component_id) REFERENCES task_component(id) ON DELETE CASCADE
);

-- Relation: Task Component: Integer
CREATE TABLE task_component_int (
	task_component_id INT,
	default_value INT,
	min_value INT,
	max_value INT,
	PRIMARY KEY(task_component_id),
	FOREIGN KEY(task_component_id) REFERENCES task_component(id) ON DELETE CASCADE
);

-- Relation: Task Component: Float
CREATE TABLE task_component_float (
	task_component_id INT,
	default_value DECIMAL,
	min_value DECIMAL,
	max_value DECIMAL,
	scale INT,
	PRIMARY KEY(task_component_id),
	FOREIGN KEY(task_component_id) REFERENCES task_component(id) ON DELETE CASCADE
);

-- Relation: Task Component: Textlabel
CREATE TABLE task_component_text (
	task_component_id INT,
	default_value VARCHAR(80),
	regex VARCHAR(80),
	PRIMARY KEY(task_component_id),
	FOREIGN KEY(task_component_id) REFERENCES task_component(id) ON DELETE CASCADE
);


-- ##### Job Task Component #####
-- Relation: Job Task Component
CREATE TABLE job_task_component (
	id SERIAL,
	--type VARCHAR(20) CHECK (type IN ('TEXT','DATE', 'INT', 'SELECTION', 'TEXTAREA')),
	job_task_id int,
	task_component_id int,
	PRIMARY KEY(id),
	FOREIGN KEY(job_task_id) REFERENCES job_task(job_workflow_item_id) ON DELETE CASCADE,
	FOREIGN KEY(task_component_id) REFERENCES task_component(id)
);

-- Relation: Job Task Component: Textlabel
CREATE TABLE job_task_component_text (
	job_task_component_id INT,
	value VARCHAR(80),
	PRIMARY KEY(job_task_component_id),
	FOREIGN KEY(job_task_component_id) REFERENCES job_task_component(id) ON DELETE CASCADE
);

-- Relation: Job Task Component: Date
CREATE TABLE job_task_component_date (
	job_task_component_id INT,
	value TIMESTAMP,
	PRIMARY KEY(job_task_component_id),
	FOREIGN KEY(job_task_component_id) REFERENCES job_task_component(id) ON DELETE CASCADE
);

-- Relation: Job Task Component: Integer
CREATE TABLE job_task_component_int (
	job_task_component_id INT,
	value INT,
	PRIMARY KEY(job_task_component_id),
	FOREIGN KEY(job_task_component_id) REFERENCES job_task_component(id) ON DELETE CASCADE
);

-- Relation: Job Task Component: Float 
CREATE TABLE job_task_component_float (
	job_task_component_id INT,
	value DECIMAL,
	PRIMARY KEY(job_task_component_id),
	FOREIGN KEY(job_task_component_id) REFERENCES job_task_component(id) ON DELETE CASCADE
);

-- Relation: Job Task Component: Textarea
CREATE TABLE job_task_component_textarea (
	job_task_component_id INT,
	value TEXT,
	PRIMARY KEY(job_task_component_id),
	FOREIGN KEY(job_task_component_id) REFERENCES job_task_component(id) ON DELETE CASCADE
);

-- Relation: Task Components eines Tasks
CREATE TABLE workflow_item_task_component (
	workflow_item_id INT,
	task_component_id INT,
	readonly BOOLEAN,
	PRIMARY KEY(workflow_item_id, task_component_id),
	FOREIGN KEY(workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(task_component_id) REFERENCES task_component(id) ON DELETE CASCADE
);

-- Relation: Zusatzattribute einer Decision
CREATE TABLE workflow_item_decision (
	workflow_item_id INT,
	next_workflow_item_on_true INT,
	condition TEXT,
	PRIMARY KEY(workflow_item_id),
	FOREIGN KEY(workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(next_workflow_item_on_true) REFERENCES workflow_item(id) ON DELETE CASCADE
) ;

-- ##### Script #####
-- Relation: Script
CREATE TABLE workflow_item_script (
	workflow_item_id INT,
	script TEXT,
	PRIMARY KEY(workflow_item_id),
	FOREIGN KEY(workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE
);

-- Relation: TaskVariables einer Decision
CREATE TABLE task_variables (
	workflow_item_id INT,
	name VARCHAR(80) NOT NULL,
	task_id INT,
	task_component_id INT,
	UNIQUE(workflow_item_id, name),
	PRIMARY KEY(workflow_item_id, task_id, task_component_id),
	FOREIGN KEY(workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(task_id) REFERENCES workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(task_component_id) REFERENCES task_component(id) ON DELETE CASCADE
);

-- RELATION: JobBranch für die Zusammenführungsäste eines Joins
CREATE TABLE join_branch (
	job_workflow_item_id INT,
	prev_workflow_item_id INT,
	PRIMARY KEY(job_workflow_item_id),
	FOREIGN KEY(job_workflow_item_id) REFERENCES job_workflow_item(id) ON DELETE CASCADE,
	FOREIGN KEY(prev_workflow_item_id) REFERENCES workflow_item(id) ON DELETE CASCADE
);


ALTER TABLE workflow ADD FOREIGN KEY (start_item_id) REFERENCES workflow_item(id) ON DELETE SET NULL;






-- WorkflowItem-Typen
INSERT INTO workflow_item_type (name) VALUES('START');
INSERT INTO workflow_item_type (name) VALUES('END');
INSERT INTO workflow_item_type (name) VALUES('TASK');
INSERT INTO workflow_item_type (name) VALUES('DECISION');
INSERT INTO workflow_item_type (name) VALUES('SCRIPT');
INSERT INTO workflow_item_type (name) VALUES('FORK');
INSERT INTO workflow_item_type (name) VALUES('JOIN');

-- TaskComponent-Typen
INSERT INTO task_component_type (name) VALUES('TEXTLABEL');
INSERT INTO task_component_type (name) VALUES('TEXTFIELD');
INSERT INTO task_component_type (name) VALUES('INT');
INSERT INTO task_component_type (name) VALUES('DATE');
INSERT INTO task_component_type (name) VALUES('FLOAT');

-- Default Admin
INSERT INTO users (username, password, prename, name, admin) VALUES('admin01', 'testpw', 'Mister', 'Admin', TRUE);
