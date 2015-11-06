package de.teamrocket.relaxo.rest.models.WorkflowDecision.request;

import java.util.List;

public class WorkflowDecisionUpdateRequest {
    private List<TaskVariableRequest> variables;
    private String condition;
    private Integer nextWorkflowItemOnTrue;

    public List<TaskVariableRequest> getVariables() {
        return variables;
    }

    public void setVariables(List<TaskVariableRequest> variables) {
        this.variables = variables;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getNextWorkflowItemOnTrue() {
        return nextWorkflowItemOnTrue;
    }

    public void setNextWorkflowItemOnTrue(Integer nextWorkflowItemOnTrue) {
        this.nextWorkflowItemOnTrue = nextWorkflowItemOnTrue;
    }
}
