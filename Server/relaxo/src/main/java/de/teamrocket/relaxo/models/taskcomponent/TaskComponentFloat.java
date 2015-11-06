package de.teamrocket.relaxo.models.taskcomponent;

import java.math.BigDecimal;

public class TaskComponentFloat extends TaskComponent {

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

    public TaskComponentFloat() {
        this.setType("FLOAT");
    }

    /**
     * Gibt den Defaultwert zurück.
     *
     * @return Defaultwert der Komponente
     */
    public BigDecimal getDefaultValue() {
        return defaultValue;
    }

    /**
     * Setzt den Defaultwert der Komponente
     *
     * @param defaultValue Defaultwert der Komponente
     */
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
