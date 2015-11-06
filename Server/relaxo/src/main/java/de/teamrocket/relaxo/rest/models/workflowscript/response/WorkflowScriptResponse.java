package de.teamrocket.relaxo.rest.models.workflowscript.response;

import java.util.List;

import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;

public class WorkflowScriptResponse {
	
    /**
     * der Nachfolger dieses Elements
     */
    private Integer nextWorkflowItemId;
    
    /**
     * Variablen, die JobTaskComponent Inhalte mit dem Script verbinden
     */
    private List<TaskVariable> variables;

    /**
     * Das Python-Script
     */
    private String script;

	public WorkflowScriptResponse(WorkflowScript workflowScript) {
		this.nextWorkflowItemId = workflowScript.getNextWorkflowItemId();
		this.variables = workflowScript.getVariables();
		this.script = workflowScript.getScript();
		
	}

	public Integer getNextWorkflowItemId() {
		return nextWorkflowItemId;
	}

	public void setNextWorkflowItemId(Integer nextWorkflowItemId) {
		this.nextWorkflowItemId = nextWorkflowItemId;
	}

	public List<TaskVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<TaskVariable> variables) {
		this.variables = variables;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
