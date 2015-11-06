package de.teamrocket.relaxo.controller;

import java.util.List;

import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;

/**
 * Controller Interface fuer WorkflowScript stellt Methoden bereit, um spezifisch auf WorkflowScript zuzugreifen.
 */
public interface WorkflowScriptController {

	/**
	 * Aktualisiert die Daten von WorkflowScript-Objekten.
	 *
	 * @param workflowScriptId die ID des WorkflowScripts
	 * @param variables Liste der Variablen
	 * @param condition der Python-Code, der ausgeführt werden soll.
	 * @throws WorkflowItemNotFoundException
	 */
	public void updateWorkflowScript(int workflowScriptId, List<TaskVariable> variables, String condition) throws WorkflowItemNotFoundException;

	/**
	 * Gibt das WorkflowScript-Objekten wieder, welches mit der übergeben ID übereinstimmt.
	 *
	 * @param workflowScriptId Die id des WorkflowScript
	 * @return das WorkflowScript-Objekten
	 * @throws WorkflowItemNotFoundException
	 */
	public WorkflowScript getWorkflowScript(int workflowScriptId) throws WorkflowItemNotFoundException;

}
