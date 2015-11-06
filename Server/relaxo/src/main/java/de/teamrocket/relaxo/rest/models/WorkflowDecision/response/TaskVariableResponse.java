package de.teamrocket.relaxo.rest.models.WorkflowDecision.response;

import de.teamrocket.relaxo.models.workflow.TaskVariable;

public class TaskVariableResponse {
    private String name;
    private int taskId;
    private int taskComponentId;

    public TaskVariableResponse(TaskVariable taskVariable) {
        this.name = taskVariable.getName();
        this.taskId = taskVariable.getTaskId();
        this.taskComponentId = taskVariable.getTaskComponentId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskComponentId() {
        return taskComponentId;
    }

    public void setTaskComponentId(int taskComponentId) {
        this.taskComponentId = taskComponentId;
    }
}
