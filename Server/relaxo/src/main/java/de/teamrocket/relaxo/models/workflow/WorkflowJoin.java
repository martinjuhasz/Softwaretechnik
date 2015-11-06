package de.teamrocket.relaxo.models.workflow;

import java.util.List;

/**
 * Klasse zur Repraesentation eines WorkflowItem-Join
 * Ein Join haelt eine Liste von WorkflowItems in die der Workflow
 * waehrend der parallelen Bearbeitung aufgeteilt war sowie sein Nachfolger Item
 */
public class WorkflowJoin extends WorkflowItem{
	/**
	 * Nachfolger Item des Join
	 */
	private int nextWorkflowItemId;

	/**
	 * Liste der WorkflowItems die zur parallelen Bearbeitung aufgeteilt wurden
	 */
	private List<Integer> previousWorkflowItemIds;
	
    public WorkflowJoin(){
        this.setType("JOIN");
    }
	
	public int getNextWorkflowItemId() {
		return nextWorkflowItemId;
	}
	public void setNextWorkflowItemId(int nextWorkflowItemId) {
		this.nextWorkflowItemId = nextWorkflowItemId;
	}
	public List<Integer> getPreviousWorkflowItemIds() {
		return previousWorkflowItemIds;
	}
	public void setPreviousWorkflowItemIds(List<Integer> previousWorkflowItemIds) {
		this.previousWorkflowItemIds = previousWorkflowItemIds;
	}
}
