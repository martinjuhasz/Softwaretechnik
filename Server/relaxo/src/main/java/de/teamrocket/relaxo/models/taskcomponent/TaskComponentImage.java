package de.teamrocket.relaxo.models.taskcomponent;

/**
 * Komponente zur Repraesentation eines Bilds
 */
public class TaskComponentImage extends TaskComponent {

    // Vars

    /**
     * Der Default-Wert.
     */
    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}