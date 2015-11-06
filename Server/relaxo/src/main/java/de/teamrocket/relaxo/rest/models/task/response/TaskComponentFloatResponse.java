package de.teamrocket.relaxo.rest.models.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponentFloat;

import java.math.BigDecimal;


/**
 * Text-Komponente fuer die Antwort des REST-Servers auf Anfragen nach Taskdetails
 */
public class TaskComponentFloatResponse extends TaskComponentResponse {

    /**
     * Der Default-Wert.
     */
    private BigDecimal defaultValue;
    
    /**
     * Der minimale Wert.
     */
    private BigDecimal minValue;

    /**
     * Der maximale Wert.
     */
    private BigDecimal maxValue;

    /**
     * Die Nachkommastellen.
     */
    private int scale;

    /**
     * Leerer Konstruktor
     */
    public TaskComponentFloatResponse() {
    }

    /**
     * Konstruktor zum befüllen des Responses
     *
     * @param taskComponent TaskComponent aus der die Response erstellt werden soll
     */
    public TaskComponentFloatResponse(TaskComponentFloat taskComponent) {
        super(taskComponent);
        this.defaultValue = taskComponent.getDefaultValue();
        this.minValue = taskComponent.getMinValue();
        this.maxValue = taskComponent.getMaxValue();
        this.scale = taskComponent.getScale();
    }

    @JsonProperty("default")
    public BigDecimal getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(BigDecimal defaultValue) {
        this.defaultValue = defaultValue;
    }

	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
}
