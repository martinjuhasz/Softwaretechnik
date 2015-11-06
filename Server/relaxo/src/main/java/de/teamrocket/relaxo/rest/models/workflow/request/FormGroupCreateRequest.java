package de.teamrocket.relaxo.rest.models.workflow.request;

/**
 * Enthält die Request Daten eines Requests zum erstellen einer neuen FormGroup
 */
public class FormGroupCreateRequest {

    /**
     * Der Name der FromGroup
     */
    private String name;

    public FormGroupCreateRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
