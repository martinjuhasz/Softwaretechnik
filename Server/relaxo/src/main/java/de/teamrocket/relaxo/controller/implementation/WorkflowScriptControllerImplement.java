package de.teamrocket.relaxo.controller.implementation;

import java.util.List;

import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.WorkflowScriptController;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;

public class WorkflowScriptControllerImplement implements WorkflowScriptController {

	// Vars

	/**
	 * Instanz des WorkflowItemService.
	 */
	private final WorkflowItemService workflowItemService;

	// Constructor
	
	@Inject
	public WorkflowScriptControllerImplement(WorkflowItemService workflowItemService){
		this.workflowItemService = workflowItemService;
	}

	// Methods

	@Override
	public WorkflowScript getWorkflowScript(int workflowScriptId) throws WorkflowItemNotFoundException {
		return workflowItemService.getWorkflowScriptById(workflowScriptId);
	}

	@Override
	public void updateWorkflowScript(int workflowScriptId, List<TaskVariable> variables, String script) throws WorkflowItemNotFoundException {
		workflowItemService.updateWorkflowScript(workflowScriptId, variables, script);
	}

}
