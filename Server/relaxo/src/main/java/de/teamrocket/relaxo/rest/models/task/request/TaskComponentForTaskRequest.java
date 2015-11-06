package de.teamrocket.relaxo.rest.models.task.request;

public class TaskComponentForTaskRequest {
    private int id;
    private boolean readOnly;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
