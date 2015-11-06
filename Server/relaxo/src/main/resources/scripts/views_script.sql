-- View zur Anzeige aller sichtbaren Workflows pro User
CREATE OR REPLACE VIEW workflow_user AS
	SELECT wf.id AS workflow_id, wf.name AS workflow_name, uug.user_id
	FROM workflow wf 
	JOIN workflow_item wfi ON wf.id = wfi.workflow_id 
	JOIN workflow_item_usergroup wfiug ON wfi.id = wfiug.workflow_item_id 
	JOIN usergroup ug ON wfiug.usergroup_id = ug.id
	JOIN user_usergroup uug ON ug.id = uug.usergroup_id
	WHERE wf.runnable = TRUE
	GROUP BY uug.user_id, wf.id
	ORDER BY wf.id;

-- View zur Anzeige aller UserGroups eines Users
CREATE OR REPLACE VIEW usergroups_user AS
	SELECT usergroup.id AS id, usergroup.name AS name, users.id AS user_id
	FROM usergroup
		JOIN user_usergroup ON usergroup.id = user_usergroup.usergroup_id
		JOIN users ON users.id = user_usergroup.user_id;
		
CREATE OR REPLACE VIEW user_in_usergroup AS
	SELECT users.*, user_usergroup.usergroup_id
	FROM users JOIN user_usergroup ON users.id = user_usergroup.user_id;

-- View zur Anzeige eines Users an Hand eines Token
CREATE OR REPLACE VIEW user_token AS
	SELECT
		id,
		username,
		password,
		prename,
		name,
		admin,
		active,
		token
	FROM TOKEN
		JOIN USERS ON TOKEN.user_id = USERS.id;

-- View zur Anzeige aller WorkflowItems, die einem User zugeordnet sind
CREATE OR REPLACE VIEW user_workflow_items AS
	SELECT wfi.workflow_id as workflow_id, wfi.id AS workflow_item_id, wfi.name AS name, wfi.type AS type, uug.user_id
	FROM workflow_item wfi
	JOIN workflow_item_usergroup wfiug ON wfi.id = wfiug.workflow_item_id
	JOIN user_usergroup uug ON wfiug.usergroup_id = uug.usergroup_id
	GROUP BY wfi.id, wfi.name, wfi.type, uug.user_id ORDER BY workflow_item_id;

-- View zur Überprüfung ob ein User Jobs in einem bestimmten Workflow starten kann
CREATE OR REPLACE VIEW isUserAbleToStartJobsForWorkflow AS
	SELECT wfi.workflow_id AS workflow_id, uug.user_id AS user_id
	FROM workflow_item wfi
		JOIN workflow_item_usergroup wfiug ON wfiug.workflow_item_id = wfi.id
		JOIN user_usergroup uug ON wfiug.usergroup_id = uug.usergroup_id
		WHERE wfi.type ='START'
	GROUP BY workflow_id, user_id;

-- View zur Anzeige aller TaskComponents (und Unterklassen) zu einem Task
CREATE OR REPLACE VIEW all_task_components AS
	SELECT wfitc.workflow_item_id AS task_id, wfitc.task_component_id, wfitc.readonly,
	tc.name, tc.comment, tc.index, tc.required, tc.type, tc.form_group_id,
	tci.default_value AS int_default_value, tci.min_value AS int_min_value, tci.max_value AS int_max_value,
	tcf.default_value AS float_default_value, tcf.min_value AS float_min_value, tcf.max_value AS float_max_value, tcf.scale AS float_scale,
	tct.default_value AS text_default_value, tct.regex AS text_regex,
	tcd.default_value AS date_default_value
	FROM workflow_item_task_component wfitc
	JOIN task_component tc ON wfitc.task_component_id = tc.id
	LEFT JOIN task_component_int tci ON tc.id = tci.task_component_id
	LEFT JOIN task_component_float tcf ON tc.id = tcf.task_component_id
	LEFT JOIN task_component_text tct ON tc.id = tct.task_component_id
	LEFT JOIN task_component_date tcd ON tc.id = tcd.task_component_id;



CREATE OR REPLACE VIEW all_components AS
	SELECT tc.id AS task_component_id,
	tc.name, tc.comment, tc.index, tc.required, tc.type, tc.form_group_id,
	tci.default_value AS int_default_value, tci.min_value AS int_min_value, tci.max_value AS int_max_value,
	tcf.default_value AS float_default_value, tcf.min_value AS float_min_value, tcf.max_value AS float_max_value, tcf.scale AS float_scale,
	tct.default_value AS text_default_value, tct.regex AS text_regex,
	tcd.default_value AS date_default_value
	FROM task_component tc
	LEFT JOIN task_component_int tci ON tc.id = tci.task_component_id
	LEFT JOIN task_component_float tcf ON tc.id = tcf.task_component_id
	LEFT JOIN task_component_text tct ON tc.id = tct.task_component_id
	LEFT JOIN task_component_date tcd ON tc.id = tcd.task_component_id
	ORDER BY tc.id;

CREATE OR REPLACE VIEW all_job_tasks AS
	SELECT jwfi.id AS job_task_id, jwfi.job_id, jwfi.workflow_item_id, jwfi.done,
	jt.start_time, jt.end_time, jt.blocker_user_id, jt.editor_user_id,
	jtc.id AS job_task_component_id, jtc.task_component_id,
	tc.type AS job_task_component_type,
	jtci.value AS int_value,
	jtcf.value AS float_value,
	jtct.value AS text_value,
	jtcd.value AS date_value
	FROM job_workflow_item jwfi
	JOIN job_task jt ON jwfi.id = jt.job_workflow_item_id
	LEFT JOIN job_task_component jtc ON jt.job_workflow_item_id = jtc.job_task_id
	LEFT JOIN task_component tc ON jtc.task_component_id = tc.id
	LEFT JOIN job_task_component_int jtci ON jtc.id = jtci.job_task_component_id
	LEFT JOIN job_task_component_float jtcf ON jtc.id = jtcf.job_task_component_id
	LEFT JOIN job_task_component_text jtct ON jtc.id = jtct.job_task_component_id
	LEFT JOIN job_task_component_date jtcd ON jtc.id = jtcd.job_task_component_id;

CREATE OR REPLACE VIEW all_job_workflowitems AS
	SELECT jwfi.job_id, jwfi.workflow_item_id, jwfi.done, wfi.workflow_id, wfi.type, wfi.name, wfi.comment, wfi.x_pos, wfi.y_pos, wfi.locked,
	nwfi.next_workflow_item_id
	FROM job_workflow_item jwfi
	JOIN workflow_item wfi ON jwfi.workflow_item_id = wfi.id
	LEFT JOIN next_workflow_item nwfi ON jwfi.workflow_item_id = nwfi.workflow_item_id;

CREATE OR REPLACE VIEW task_components_and_type AS
	SELECT DISTINCT task_component_id, name, comment, index, required, type, form_group_id,
	int_default_value, int_min_value, int_max_value,
	float_default_value, float_min_value, float_max_value, float_scale,
	text_default_value, text_regex,
	date_default_value
	FROM all_task_components ORDER BY task_component_id;

CREATE OR REPLACE VIEW workflow_items_workflow AS
	SELECT wfi.workflow_id,
	       wfi.id AS workflow_item_id,
	       wfi.type,
	       wfi.name,
	       wfi.comment,
	       wfi.x_pos,
	       wfi.y_pos,
	       wfi.locked,
	       nwfi.next_workflow_item_id
	FROM workflow_item wfi
	LEFT JOIN next_workflow_item nwfi ON wfi.id = nwfi.workflow_item_id;

CREATE OR REPLACE VIEW workflow_item_task_component_form_group AS
	select wfitc.workflow_item_id, tc.form_group_id, tc.id AS task_component_id
	FROM workflow_item_task_component wfitc
	JOIN task_component tc ON wfitc.task_component_id = tc.id;

CREATE OR REPLACE VIEW jobs_for_task AS
	SELECT DISTINCT job.id, job.workflow_id, job.creator_id, job.start_time, job.end_time, jwfi.workflow_item_id AS task_id
	FROM job
	JOIN job_workflow_item jwfi ON job.id = jwfi.job_id
	JOIN job_task jt ON jwfi.id = jt.job_workflow_item_id
	WHERE jwfi.done = False;

CREATE OR REPLACE VIEW active_job_tasks AS
	SELECT job_task_id, job_id, workflow_item_id, job_task_component_id,
			task_component_id, job_task_component_type, int_value, float_value, text_value, date_value
	FROM all_job_tasks WHERE done=False;

CREATE OR REPLACE VIEW inactive_job_tasks AS
	SELECT job_task_id, job_id, workflow_item_id, job_task_component_id,
			task_component_id, job_task_component_type, int_value, float_value, text_value, date_value
	FROM all_job_tasks WHERE done=True;

CREATE OR REPLACE VIEW act_job_task_component_value AS
	SELECT t1.job_id, t1.task_component_id, t1.int_value, t1.float_value, t1.text_value, t1.date_value 
	FROM all_job_tasks t1 
	LEFT JOIN all_job_tasks t2 ON (t1.task_component_id = t2.task_component_id AND t1.end_time < t2.end_time) 
	WHERE t1.end_time IS NOT NULL AND t2.job_task_component_id IS NULL;
	
CREATE OR REPLACE VIEW active_join_branches AS
	SELECT jwfi.id AS join_branch_id, jwfi.workflow_item_id, jwfi.job_id, jwfi.done,
	jb.prev_workflow_item_id
	FROM job_workflow_item jwfi
	JOIN join_branch jb ON jwfi.id = jb.job_workflow_item_id
	WHERE jwfi.done = FALSE;