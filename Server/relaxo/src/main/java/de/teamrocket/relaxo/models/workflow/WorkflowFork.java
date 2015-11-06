package de.teamrocket.relaxo.models.workflow;

import java.util.List;

/**
 * Klasse zur Repraesentation eines WorkflowItem-Fork
 * Ein Fork haelt eine Liste von WorkflowItems in die der Workflow
 * waehrend der parallelen Bearbeitung aufgeteilt wird
 */
public class WorkflowFork extends WorkflowItem {

	/**
	 * Liste der WorflowItems die in eine parallele Bearbeitung aufgeteilt werden
	 */
    private List<Integer> nextWorkflowItems;
    
    public WorkflowFork(){
        this.setType("FORK");
    }

	public List<Integer> getNextWorkflowItems() {
		return nextWorkflowItems;
	}

	public void setNextWorkflowItems(List<Integer> nextWorkflowItems) {
		this.nextWorkflowItems = nextWorkflowItems;
	}

}
