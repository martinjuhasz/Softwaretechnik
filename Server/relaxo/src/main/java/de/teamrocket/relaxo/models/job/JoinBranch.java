package de.teamrocket.relaxo.models.job;

/**
 * Ein JoinBranch ist ein JobWorkflowItem für jeden eingehenden Ast eines WorkflowJoins
 */
public class JoinBranch extends JobWorkflowItem {

	/**
	 * Die Id des vorhergenden WorklfowItems des WorkflowJoins
	 */
	private int prevWorkflowItemId;

	public int getPrevWorkflowItemId() {
		return prevWorkflowItemId;
	}

	public void setPrevWorkflowItemId(int prevWorkflowItemId) {
		this.prevWorkflowItemId = prevWorkflowItemId;
	}
}