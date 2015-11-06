package de.teamrocket.relaxo.rest.models.job.response;


/**
 * Request Objekt einer Clientanfrage mit Inhalten eines einelnen Komponenten
 */
public abstract class JobTaskComponentResponse {

    //

    /**
     * Der Typ der Komponente
     */
    private String type;
    /**
     * Die ID des dazugehörigen TaskComponent
     */
    private int taskComponentID;
    /**
     * Der Inhalt des JobComponents
     */
    //

    /**
     * Konstruktor zum befüllen des JobComponentResponse
     *
     * @param jobTaskComponent die Componente die zum füllen verwendet wird
     */
    public JobTaskComponentResponse(String type, int taskComponentId) {
        this.type = type;
        this.taskComponentID = taskComponentId;
    }

    //

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTaskComponentID() {
        return taskComponentID;
    }

    public void setTaskComponentID(int taskComponentID) {
        this.taskComponentID = taskComponentID;
    }
}
