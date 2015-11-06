package de.teamrocket.relaxo.rest.models.task.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;

/**
 * Task-Komponente fuer die Antwort des REST-Servers auf Anfragen nach Taskdetails
 */


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TaskComponentTextResponse.class, name = "TaskComponentText")
})
public abstract class TaskComponentResponse {

    // Vars

    /**
     * Die Id der Komponente
     */
    private int id;
    /**
     * Die Bezeichnung dieser Komponente
     */
    private String name;
    /**
     * Ein Kommentar zu dieser Komponente
     */
    private String comment;
    /**
     * Position der Komponente im Formular
     */
    private int order;
    /**
     * Gibt an, ob diese Komponente im Formular ausgefüllt werden muss.
     */
    private boolean required;
    /**
     * Der Typ der Komponente
     */
    private String type;

    private boolean readOnly;

    /**
     * Leerer Konstruktor
     */
    public TaskComponentResponse() {
    }

    /**
     * Konstruktor zum befüllen des Responses
     *
     * @param taskComponent TaskComponent aus der die Response erstellt werden soll
     */
    public TaskComponentResponse(TaskComponent taskComponent) {
        this.id = taskComponent.getId();
        this.name = taskComponent.getName();
        this.comment = taskComponent.getComment();
        this.required = taskComponent.isRequired();
        this.order = taskComponent.getIndex();
        this.type = taskComponent.getClass().getSimpleName();
        this.readOnly = taskComponent.isReadOnly();
    }

    // Getter

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isRequired() {
        return required;
    }

    // Setter

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }


}
