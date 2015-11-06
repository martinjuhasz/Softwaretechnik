package de.teamrocket.relaxo.models.workflow;

import java.util.List;

/**
 * Klasse zur Repraesentation eines WorkflowItem-Script
 * Ein Script haelt eine Liste von Variablen, sowie das Nachfolger Item
 * und ein auszufuehrendes Python Script
 */
public class WorkflowScript extends WorkflowItem {

    // VARS

    /**
     * der Nachfolger dieses Task
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

    // CONSTRUCT

    public WorkflowScript() {
        this.setType("SCRIPT");
    }

    // GETTER / SETTER

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
