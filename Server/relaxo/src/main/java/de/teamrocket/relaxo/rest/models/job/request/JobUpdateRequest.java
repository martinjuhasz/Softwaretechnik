package de.teamrocket.relaxo.rest.models.job.request;

import java.util.List;

/**
 * Anfageobjekt der Clients um einen Job zu updaten
 */
public class JobUpdateRequest {

    //

    /**
     * Die ID des zugehörigen Tasks
     */
    private int taskId;

    /**
     * Liste der Componenten die zu updaten sind
     */
    private List<JobUpdateComponentRequest> components;

    //

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public List<JobUpdateComponentRequest> getComponents() {
        return components;
    }

    public void setComponents(List<JobUpdateComponentRequest> components) {
        this.components = components;
    }
}
