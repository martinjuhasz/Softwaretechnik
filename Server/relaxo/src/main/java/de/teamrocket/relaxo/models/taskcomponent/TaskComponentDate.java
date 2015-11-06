package de.teamrocket.relaxo.models.taskcomponent;

import java.util.Date;

/**
 * Komponente zur Repraesentation eines Datums
 */
public class TaskComponentDate extends TaskComponent {

    /**
     * Der Default-Wert.
     */
    private Date defaultValue;

    public TaskComponentDate() {
        this.setType("DATE");
    }

    public Date getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Date defaultValue) {
        this.defaultValue = defaultValue;
    }
}