package de.teamrocket.relaxo.rest.models.workflow.request;

/**
 * Wrapper Klasse für ankommende Requests zum erstellen eines Workflows
 */
public class WorkflowCreateRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
