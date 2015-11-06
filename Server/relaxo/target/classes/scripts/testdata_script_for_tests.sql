-- -- User / Usergruppen

-- User
INSERT INTO users (username, password, prename, name, admin) VALUES('mmoel001', 'testpw', 'Moritz', 'Moeller', TRUE); -- 1
INSERT INTO users (username, password, prename, name, admin) VALUES('hvogt001', 'testpw', 'Henry', 'Vogt', TRUE);
INSERT INTO users (username, password, prename, name, admin) VALUES('paltm001', 'testpw', 'Philipp', 'Altmeyer', FALSE);
INSERT INTO users (username, password, prename, name, admin) VALUES('mweil002', 'testpw', 'Michael', 'Weilbächer', FALSE);
INSERT INTO users (username, password, prename, name, admin) VALUES('mjuha001', 'testpw', 'Martin', 'Juhasz', TRUE);
INSERT INTO users (username, password, prename, name, admin) VALUES('jthei001', 'testpw', 'Jonas', 'Theis', FALSE);
INSERT INTO users (username, password, prename, name, admin) VALUES('jwolf001', 'testpw', 'Johannes', 'Wolf', TRUE);
INSERT INTO users (username, password, prename, name, admin) VALUES('cmard001', 'testpw', 'Cengiz', 'Mardin', FALSE);

-- Usergruppen
INSERT INTO usergroup (name) VALUES('Geeks'); -- 1
INSERT INTO usergroup (name) VALUES('Nerds');
INSERT INTO usergroup (name) VALUES('Gamer');

INSERT INTO user_usergroup VALUES (1,1);
INSERT INTO user_usergroup VALUES (1,2);
INSERT INTO user_usergroup VALUES (1,3);
INSERT INTO user_usergroup VALUES (2,3);
INSERT INTO user_usergroup VALUES (3,2);
INSERT INTO user_usergroup VALUES (4,1);
INSERT INTO user_usergroup VALUES (4,2);
INSERT INTO user_usergroup VALUES (4,3);
INSERT INTO user_usergroup VALUES (5,2);
INSERT INTO user_usergroup VALUES (5,3);
INSERT INTO user_usergroup VALUES (6,1);
INSERT INTO user_usergroup VALUES (7,1);
INSERT INTO user_usergroup VALUES (8,2);

-- Token
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcdews',1);
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcdewz',2);
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcdewu',3);
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcdewc',4);
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcdewx',5);
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcdewy',6);
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcd3#9',7);
INSERT INTO token VALUES ('87tghjkmnbvcxsw45678iokjnbvcd4+t',8);


-- -- Workflows & WorkflowItems

-- Support-System
INSERT INTO workflow (name, creator_id, creation_date, runnable) VALUES('Support-System',    1, '2012-02-02 10:23:54', TRUE); -- 1

INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'START', 'Firestarter', 'I am a Start-Item', 50, 120); -- 1
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'TASK',  'Enter Name', 'I am a task', 150, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'TASK',  'Enter Address', 'I am a task too', 250, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'END',   'The End', 'I am a End-Item', 350, 120);

UPDATE workflow SET start_item_id=1 WHERE id=1;
INSERT INTO next_workflow_item VALUES(1,2);
INSERT INTO next_workflow_item VALUES(2,3);
INSERT INTO next_workflow_item VALUES(3,4);


-- Hotelreservierung
INSERT INTO workflow (name, creator_id, creation_date, runnable) VALUES('Hotelreservierung', 2, '2030-02-02 10:23:54', FALSE); -- 2

INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(2, 'START', 'Firestarter2', 'I am a Start-Item', 50, 120); -- 5
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(2, 'TASK',  'Nicht Fork', 'I am a fork', 150, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(2, 'TASK',  'Check Textfield2', 'I am a task', 250, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(2, 'TASK',  'Check Radio Button2', 'I am a task too', 350, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(2, 'TASK',  'Nicht Join', 'I am a join', 450, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(2, 'TASK',  'Check Textfield2', 'I am a task', 550, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(2, 'END',   'The End2', 'I am a End-Item', 650, 120);

UPDATE workflow SET start_item_id=5 WHERE id=2;
INSERT INTO next_workflow_item VALUES(5,6);
INSERT INTO next_workflow_item VALUES(6,7);
INSERT INTO next_workflow_item VALUES(7,8);
INSERT INTO next_workflow_item VALUES(8,9);
INSERT INTO next_workflow_item VALUES(9,10);
INSERT INTO next_workflow_item VALUES(10,11);


-- Kreditvergabe
INSERT INTO workflow (name, creator_id, creation_date, runnable) VALUES('Kreditvergabe', 3, '2015-01-01 13:30:10', TRUE); -- 3

INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(3, 'START', 'Startitem', 'KreditvergabeStart',0, 0); -- 12
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(3, 'TASK', 'Kreditbetrag', 'Wunschbetrag des Kredits',0, 0);
INSERT INTO workflow_item (workflow_id, type, x_pos, y_pos) VALUES(3, 'DECISION', 0, 0); -- 14
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(3, 'TASK', 'Kredit bewilligt', 'Kontodaten eintragen', 0, 0);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(3, 'TASK', 'Kredit abgelehnt', 'Ablehnungsgrund erfinden', 0, 0);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(3, 'END',   'Ende 1', 'gutes Ende', 650, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(3, 'END',   'Ende 2', 'schlechtes Ende', 650, 120);

UPDATE workflow SET start_item_id=12 WHERE id=3;
INSERT INTO next_workflow_item VALUES(12, 13);
INSERT INTO next_workflow_item VALUES(13, 14);
INSERT INTO next_workflow_item VALUES(14, 15);
INSERT INTO next_workflow_item VALUES(14, 16);
INSERT INTO next_workflow_item VALUES(15, 17);
INSERT INTO next_workflow_item VALUES(16, 18);


-- Scripting
INSERT INTO workflow (name, creator_id, creation_date, runnable) VALUES('Scripting', 8, '2015-01-22 10:43:54', FALSE); -- 4

INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(4, 'START', 'Script-Kiddi-Start', 'I am a Start-Item', 50, 120); -- 19
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(3, 'TASK', 'Arbeitsverweigerer', 'Wer arbeitet zu wenig',0, 0);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(4, 'SCRIPT',  'Do Script', 'I am a script', 150, 120);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(4, 'END',   'The SCRIPT-End', 'I am a End-Item', 350, 120);
INSERT INTO workflow_item_script VALUES (20, 'print "hello world"');

UPDATE workflow SET start_item_id=19 WHERE id=4;
INSERT INTO next_workflow_item VALUES(19, 20);
INSERT INTO next_workflow_item VALUES(20, 21);
INSERT INTO next_workflow_item VALUES(21, 22);


-- -- Weiteres

-- FormGroups
INSERT INTO form_group (name, workflow_id) VALUES('Kontaktdaten',1);
INSERT INTO form_group (name, workflow_id) VALUES('Kontodaten',2);
INSERT INTO form_group (name, workflow_id) VALUES('Kreditbetrag',3);
INSERT INTO form_group (name, workflow_id) VALUES('Kontodaten',3);
INSERT INTO form_group (name, workflow_id) VALUES('Ablehnnungsgrund',3);
INSERT INTO form_group (name, workflow_id) VALUES('Arbeitsverweigerer',4);


INSERT INTO workflow_item_usergroup VALUES (1,1);
INSERT INTO workflow_item_usergroup VALUES (1,2);
INSERT INTO workflow_item_usergroup VALUES (1,3);
INSERT INTO workflow_item_usergroup VALUES (2,2);
INSERT INTO workflow_item_usergroup VALUES (2,3);
INSERT INTO workflow_item_usergroup VALUES (3,1);
INSERT INTO workflow_item_usergroup VALUES (4,1);
INSERT INTO workflow_item_usergroup VALUES (5,2);
INSERT INTO workflow_item_usergroup VALUES (6,1);
INSERT INTO workflow_item_usergroup VALUES (6,2);
INSERT INTO workflow_item_usergroup VALUES (7,2);
INSERT INTO workflow_item_usergroup VALUES (8,1);
INSERT INTO workflow_item_usergroup VALUES (9,2);
INSERT INTO workflow_item_usergroup VALUES (10,1);
INSERT INTO workflow_item_usergroup VALUES (10,3);
INSERT INTO workflow_item_usergroup VALUES (11,2);
INSERT INTO workflow_item_usergroup VALUES (12,1);
INSERT INTO workflow_item_usergroup VALUES (13,1);
INSERT INTO workflow_item_usergroup VALUES (15,1);
INSERT INTO workflow_item_usergroup VALUES (16,1);

-- TaskComponents
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Nachname', 'Nachname Eingabefeld', 1, TRUE, 'TEXTLABEL', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Vorname', 'Vorname Eingabefeld', 2, TRUE, 'TEXTLABEL', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Straße', 'Straße Eingabefeld', 3, TRUE, 'TEXTLABEL', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('PLZ', 'Postleitzahl', 4, TRUE, 'INT', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Stadt', 'Stadt Eingabefeld', 5, TRUE, 'TEXTLABEL', 1);

INSERT INTO task_component_text VALUES(1, 'Ihr Nachname', '^([a-z]|[A-Z]|\s)+$'); -- 1
INSERT INTO task_component_text VALUES(2, 'Ihr Vorname', '^([a-z]|[A-Z]|\s)+$');
INSERT INTO task_component_text VALUES(3, 'Straße und Hausnr.', '^([a-z]|[A-Z]|\s)+[0-9]+$');
INSERT INTO task_component_int VALUES(4, 65197, 10000, 99999);
INSERT INTO task_component_text VALUES(5, 'Stadt', '^([a-z]|[A-Z]|\s)*$');

INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Konto Nr', 'KontoNr Eingabefeld', 1, TRUE, 'INT', 2);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Bankleitzahl', 'BLZ Eingabefeld', 2, TRUE, 'INT', 2);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Konto Inhaber', 'Inhaber Eingabefeld', 3, TRUE, 'TEXTLABEL', 2);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Start-Datum', 'Datumsfeld', 4, FALSE, 'DATE', 2);

INSERT INTO task_component_int VALUES(6, 1234567, 0, 99999999);
INSERT INTO task_component_int VALUES(7, 7654321, 0, 99999999);
INSERT INTO task_component_text VALUES(8, 'Inhaber', '^([a-z]|[A-Z]|\s)+$');
INSERT INTO task_component_date VALUES(9, '1970-01-01 00:00:00');

INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Kreditbetrag', 'Kreditbetrag Eingabefeld', 1, TRUE, 'FLOAT', 3);
INSERT INTO task_component_float VALUES(10, 100.00, 0.00, 10000.00, 2);

INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Konto Nr', 'KontoNr Eingabefeld', 1, TRUE, 'INT', 4);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Bankleitzahl', 'BLZ Eingabefeld', 2, TRUE, 'INT', 4);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Konto Inhaber', 'Inhaber Eingabefeld', 3, TRUE, 'TEXTLABEL', 4);

INSERT INTO task_component_int VALUES(11, 1234567, 0, 99999999);
INSERT INTO task_component_int VALUES(12, 7654321, 0, 99999999);
INSERT INTO task_component_text VALUES(13, 'Inhaber', '^([a-z]|[A-Z]|\s)+$');

INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Kreditablehnnung', 'Ablehnungsgrund', 1, TRUE, 'TEXTLABEL', 5);
INSERT INTO task_component_text VALUES(14, 'Zu arm', '.*');

INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Arbeitsverweigerer', 'Wer arbeitet zu wenig', 1, TRUE, 'TEXTLABEL', 4);
INSERT INTO task_component_text VALUES (15, 'Slackbot', '.*');

INSERT INTO workflow_item_task_component VALUES(2,1);
INSERT INTO workflow_item_task_component VALUES(2,2);
INSERT INTO workflow_item_task_component VALUES(3,1);
INSERT INTO workflow_item_task_component VALUES(3,2);
INSERT INTO workflow_item_task_component VALUES(3,3);
INSERT INTO workflow_item_task_component VALUES(3,4);
INSERT INTO workflow_item_task_component VALUES(3,5);

INSERT INTO workflow_item_task_component VALUES(7,1);
INSERT INTO workflow_item_task_component VALUES(7,2);
INSERT INTO workflow_item_task_component VALUES(7,3);
INSERT INTO workflow_item_task_component VALUES(8,1);
INSERT INTO workflow_item_task_component VALUES(8,2);
INSERT INTO workflow_item_task_component VALUES(10,1);
INSERT INTO workflow_item_task_component VALUES(10,2);
INSERT INTO workflow_item_task_component VALUES(10,4);
INSERT INTO workflow_item_task_component VALUES(10,9);

INSERT INTO workflow_item_task_component VALUES(13,10);
INSERT INTO workflow_item_task_component VALUES(15,11);
INSERT INTO workflow_item_task_component VALUES(15,12);
INSERT INTO workflow_item_task_component VALUES(15,13);
INSERT INTO workflow_item_task_component VALUES(16,14);

INSERT INTO workflow_item_task_component VALUES(20,15);