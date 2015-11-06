package de.teamrocket.relaxo.models.taskcomponent;

/**
 * Komponente zur Repraesentation eines Integer-Wertes
 */
public class TaskComponentInteger extends TaskComponent {

    // Vars

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

    public TaskComponentInteger() {
        this.setType("INT");
    }

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