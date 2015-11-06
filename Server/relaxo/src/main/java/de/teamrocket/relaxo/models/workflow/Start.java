package de.teamrocket.relaxo.models.workflow;


/**
 * Klasse zur Repraesentation eines Workflow-Start
 */
public class Start extends WorkflowItem {
    private Integer nextWorkflowItemId;

    public Start() {
        this.setType("START");
    }

    public Integer getNextWorkflowItemId() {
        return nextWorkflowItemId;
    }

    public void setNextWorkflowItemId(Integer nextWorkflowItemId) {
        this.nextWorkflowItemId = nextWorkflowItemId;
    }
}
