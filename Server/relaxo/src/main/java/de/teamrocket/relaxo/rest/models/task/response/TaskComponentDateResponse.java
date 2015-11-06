package de.teamrocket.relaxo.rest.models.task.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponentDate;

import java.util.Date;

/**
 * Date-Komponente fuer die Antwort des REST-Servers auf Anfragen nach Taskdetails
 */
public class TaskComponentDateResponse extends TaskComponentResponse {

    /**
     * Der Default-Wert.
     */
    private Date defaultValue;

    public TaskComponentDateResponse() {
    }

    /**
     * Konstruktor zum befüllen des Responses
     *
     * @param taskComponent TaskComponent aus der die Response erstellt werden soll
     */
    public TaskComponentDateResponse(TaskComponentDate taskComponent) {
        super(taskComponent);
        this.defaultValue = taskComponent.getDefaultValue();
    }

    @JsonProperty("default")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
    public Date getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Date defaultValue) {
        this.defaultValue = defaultValue;
    }
}
