-- -- User / Usergruppen

-- User
INSERT INTO users (username, password, prename, name, admin) VALUES('user01', 'testpw', 'Mister', 'User 01', FALSE);
INSERT INTO users (username, password, prename, name, admin) VALUES('user02', 'testpw', 'Mister', 'User 02', FALSE);
INSERT INTO users (username, password, prename, name, admin) VALUES('user03', 'testpw', 'Mister', 'User 03', FALSE);

-- Usergruppen
INSERT INTO usergroup (name) VALUES('Bestellannahme');
INSERT INTO usergroup (name) VALUES('Lager');
INSERT INTO usergroup (name) VALUES('Warenausgabe');

INSERT INTO user_usergroup VALUES (2,1);
INSERT INTO user_usergroup VALUES (3,2);
INSERT INTO user_usergroup VALUES (4,3);


-- -- Workflows & WorkflowItems

-- Warensystem
INSERT INTO workflow (name, creator_id, creation_date, runnable) VALUES('Warensystem', 1, '2015-01-27 10:23:54', FALSE);

INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'START', 'Startitem', 'No comment', 7, 126);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'TASK',  'Bestellannahme', 'No comment', 104, 85);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'TASK',  'Lagerprüfung', 'No comment', 217, 85);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'DECISION',  'Ware verfügbar?', 'No comment', 363, 110);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'END',   'Ende False', 'No comment', 376, 278);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'TASK',  'Datenüberprüfung', 'No comment', 511, 87);
INSERT INTO workflow_item (workflow_id, type, name, comment, x_pos, y_pos) VALUES(1, 'END',   'Ende True', 'No comment', 664, 126);

UPDATE workflow SET start_item_id=1 WHERE id=1;

INSERT INTO next_workflow_item VALUES(1,2);
INSERT INTO next_workflow_item VALUES(2,3);
INSERT INTO next_workflow_item VALUES(3,4);
INSERT INTO next_workflow_item VALUES(4,5);
INSERT INTO next_workflow_item VALUES(4,6);
INSERT INTO next_workflow_item VALUES(6,7);


-- -- Weiteres

-- FormGroups
INSERT INTO form_group (name, workflow_id) VALUES('Kontaktdaten',1);
INSERT INTO form_group (name, workflow_id) VALUES('Bestellung',1);
INSERT INTO form_group (name, workflow_id) VALUES('Warenbestand',1);


INSERT INTO workflow_item_usergroup VALUES (1,1);
INSERT INTO workflow_item_usergroup VALUES (2,1);
INSERT INTO workflow_item_usergroup VALUES (3,2);
INSERT INTO workflow_item_usergroup VALUES (6,3);


-- TaskComponents
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Nachname', 'Nachname Eingabefeld', 1, TRUE, 'TEXTLABEL', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Vorname', 'Vorname Eingabefeld', 2, TRUE, 'TEXTLABEL', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Straße', 'Straße Eingabefeld', 3, TRUE, 'TEXTLABEL', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('PLZ', 'Postleitzahl', 4, TRUE, 'INT', 1);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Stadt', 'Stadt Eingabefeld', 5, TRUE, 'TEXTLABEL', 1);
INSERT INTO task_component_text VALUES(1, 'Kunden - Nachname', '^([a-z]|[A-Z]|\s)+$');
INSERT INTO task_component_text VALUES(2, 'Kunden - Vorname', '^([a-z]|[A-Z]|\s)+$');
INSERT INTO task_component_text VALUES(3, 'Straße und Hausnr.', '^[äöüßÄÖÜA-Za-z]+\s[0-9]+$');
INSERT INTO task_component_int VALUES(4, 65197, 10000, 99999);
INSERT INTO task_component_text VALUES(5, 'Stadt', '^([a-z]|[A-Z]|\s)*$');

INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Artikelnummer', 'Artikelnummer', 1, TRUE, 'INT', 2);
INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Artikelname', 'Artikelname', 2, TRUE, 'TEXTLABEL', 2);
INSERT INTO task_component_int VALUES(6, 1234, 1000, 9999);
INSERT INTO task_component_text VALUES(7, 'Artikelname', '^([a-z]|[A-Z]|\s)+$');

INSERT INTO task_component (name, comment, index, required, type, form_group_id) VALUES('Warenbestand', 'Ist Ware verfügbar?', 1, TRUE, 'TEXTLABEL', 3);
INSERT INTO task_component_text VALUES(8, 'Verfügbar? (Ja / Nein)', '^(Ja|Nein)$');


INSERT INTO workflow_item_task_component VALUES(2,1,FALSE);
INSERT INTO workflow_item_task_component VALUES(2,2,FALSE);
INSERT INTO workflow_item_task_component VALUES(2,3,FALSE);
INSERT INTO workflow_item_task_component VALUES(2,4,FALSE);
INSERT INTO workflow_item_task_component VALUES(2,5,FALSE);
INSERT INTO workflow_item_task_component VALUES(2,6,FALSE);
INSERT INTO workflow_item_task_component VALUES(2,7,FALSE);

INSERT INTO workflow_item_task_component VALUES(3,6, TRUE);
INSERT INTO workflow_item_task_component VALUES(3,7, TRUE);
INSERT INTO workflow_item_task_component VALUES(3,8, FALSE);

INSERT INTO workflow_item_task_component VALUES(6,1, TRUE);
INSERT INTO workflow_item_task_component VALUES(6,2, TRUE);
INSERT INTO workflow_item_task_component VALUES(6,3, TRUE);
INSERT INTO workflow_item_task_component VALUES(6,4, TRUE);
INSERT INTO workflow_item_task_component VALUES(6,5, TRUE);
INSERT INTO workflow_item_task_component VALUES(6,6, TRUE);
INSERT INTO workflow_item_task_component VALUES(6,7, TRUE);

INSERT INTO task_variables VALUES(4, 'vorhanden', 3, 8);
INSERT INTO workflow_item_decision VALUES(4, 6, 'vorhanden == "Ja"');