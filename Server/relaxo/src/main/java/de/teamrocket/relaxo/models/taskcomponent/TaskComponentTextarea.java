package de.teamrocket.relaxo.models.taskcomponent;

/**
 * Komponente zur Repraesentation eines Textfelds
 */
public class TaskComponentTextarea extends TaskComponent {

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