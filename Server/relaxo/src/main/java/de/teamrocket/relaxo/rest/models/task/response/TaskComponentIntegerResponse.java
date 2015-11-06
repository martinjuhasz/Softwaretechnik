package de.teamrocket.relaxo.rest.models.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponentInteger;

/**
 * Integer-Komponente fuer die Antwort des REST-Servers auf Anfragen nach Taskdetails
 */
public class TaskComponentIntegerResponse extends TaskComponentResponse {

    /**
     * Der Default-Wert.
     */
    private int defaultValue;

    /**
     * Der minimale Wert.
     */
    private int minValue;

    /**
     * Der maximale Wert.
     */
    private int maxValue;

    public TaskComponentIntegerResponse() {
    }

    /**
     * Konstruktor zum befüllen des Responses
     *
     * @param taskComponent TaskComponent aus der die Response erstellt werden soll
     */
    public TaskComponentIntegerResponse(TaskComponentInteger taskComponent) {
        super(taskComponent);
        this.defaultValue = taskComponent.getDefaultValue();
        this.minValue = taskComponent.getMinValue();
        this.maxValue = taskComponent.getMaxValue();
    }

    @JsonProperty("default")
    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
