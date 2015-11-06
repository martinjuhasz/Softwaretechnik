package de.teamrocket.relaxo.rest.models.workflow.response;

/**
 * Wrapper Klasse für eine Antwort nach dem Erstellen eines Workflows
 */
public class CreateWorkflowResponse {

    private int id;
    private String name;

    public CreateWorkflowResponse(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
