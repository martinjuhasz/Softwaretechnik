package de.teamrocket.relaxo.rest.models.workflow.response;


import de.teamrocket.relaxo.models.workflow.Task;

/**
 * Antwort des REST-Servers auf Taskanfragen
 */
public class TaskResponse {

    /**
     * Die ID des TaskResponses
     */
    private int id;
    /**
     * Der Name des Tasks
     */
    private String name;

    //

    /**
     * leerer Konstruktor
     */
    public TaskResponse() {
    }

    /**
     * Erstellt einen TaskResponse anhand des übergebenen Tasks
     *
     * @param task das zugehörige Taskobjekt
     */
    public TaskResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
    }

    //

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
