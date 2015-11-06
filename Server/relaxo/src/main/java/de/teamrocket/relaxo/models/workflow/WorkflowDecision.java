package de.teamrocket.relaxo.models.workflow;

import java.util.List;

/**
 * Das WorkflowItem, das eine Entscheidung im Workflow ermöglicht.
 * Je nach Auswertung der Bedingung (als Python-Code) wird zu einem von den folgenden WorkflowItems weitergerückt.
 */
public class WorkflowDecision extends WorkflowItem {
	/**
	 * Die Liste der nachfolgenden WorkflowItems der Decision (2 für einen funktionierden Workflow)
	 */
    private List<Integer> nextWorkflowItems;
    /**
     * Die Id des WorkflowItems, zu dem eine positive Auswertung der Bedingung weitergerückt wird
     */
    private Integer nextWorkflowItemOnTrue;
    /**
     * Die TaskVariablen für die Bedingung
     */
    private List<TaskVariable> variables;
    /**
     * Der Python-Code, der als Bedingung ausgewertet wird
     */
    private String condition;

    public WorkflowDecision() {
        this.setType("DECISION");
    }

    public List<Integer> getNextWorkflowItems() {
        return nextWorkflowItems;
    }

    public void setNextWorkflowItems(List<Integer> nextWorkflowItems) {
        this.nextWorkflowItems = nextWorkflowItems;
    }

    public Integer getNextWorkflowItemOnTrue() {
        return nextWorkflowItemOnTrue;
    }

    public void setNextWorkflowItemOnTrue(Integer nextWorkflowItemOnTrue) {
        this.nextWorkflowItemOnTrue = nextWorkflowItemOnTrue;
    }

    public List<TaskVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<TaskVariable> variables) {
        this.variables = variables;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
