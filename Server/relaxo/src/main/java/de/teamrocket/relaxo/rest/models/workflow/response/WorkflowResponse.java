package de.teamrocket.relaxo.rest.models.workflow.response;


import de.teamrocket.relaxo.models.workflow.Workflow;

/**
 * Antwort des REST-Server aus Workflow Anfragen
 */
public class WorkflowResponse {

    //

    /**
     * Die ID des Workflows
     */
    private int id;
    /**
     * Der Name des Workflows
     */
    private String name;

    /**
     * Gibt an, ob der Benutzer einen Job aus diesem Workflow erstellen darf.
     */
    private boolean userCanCreateJob;

    //

    /**
     * leerer Konstruktor
     */
    public WorkflowResponse() {
    }

    /**
     * Erstellt einen WorkflowResponse anhand des übergebenen Workflows
     *
     * @param workflow der repräsentierte Workflow
     */
    public WorkflowResponse(Workflow workflow) {
        this.id = workflow.getId();
        this.name = workflow.getName();
    }

    //

    public String getName() {
        return name;
    }


    public boolean getUserCanCreateJob() {
        return userCanCreateJob;
    }

    public void setUserCanCreateJob(boolean userCanCreateJob) {
        this.userCanCreateJob = userCanCreateJob;
    }


    public int getId() {
        return id;
    }
}
