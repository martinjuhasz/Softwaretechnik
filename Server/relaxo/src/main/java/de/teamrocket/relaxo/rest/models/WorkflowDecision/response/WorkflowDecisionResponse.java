package de.teamrocket.relaxo.rest.models.WorkflowDecision.response;

import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;

import java.util.LinkedList;
import java.util.List;

public class WorkflowDecisionResponse {

    private List<TaskVariableResponse> variables;
    private String condition;
    private Integer nextWorkflowItemOnTrue;

    public WorkflowDecisionResponse(WorkflowDecision workflowDecision) {
        this.condition = workflowDecision.getCondition();
        this.nextWorkflowItemOnTrue = workflowDecision.getNextWorkflowItemOnTrue();
        this.variables = new LinkedList<>();
        for (TaskVariable taskVariable : workflowDecision.getVariables()) {
            TaskVariableResponse response = new TaskVariableResponse(taskVariable);
            this.variables.add(response);
        }
    }

    public List<TaskVariableResponse> getVariables() {
        return variables;
    }

    public void setVariables(List<TaskVariableResponse> variables) {
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
