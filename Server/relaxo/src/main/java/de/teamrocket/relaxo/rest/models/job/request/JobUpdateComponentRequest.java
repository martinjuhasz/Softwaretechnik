package de.teamrocket.relaxo.rest.models.job.request;

public class JobUpdateComponentRequest {
    private int taskComponentId;
    private String value;

    public JobUpdateComponentRequest() {

    }

    public JobUpdateComponentRequest(int taskComponentId, String value) {
        this.taskComponentId = taskComponentId;
        this.value = value;
    }

    public int getTaskComponentId() {
        return taskComponentId;
    }

    public void setTaskComponentId(int taskComponentId) {
        this.taskComponentId = taskComponentId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
