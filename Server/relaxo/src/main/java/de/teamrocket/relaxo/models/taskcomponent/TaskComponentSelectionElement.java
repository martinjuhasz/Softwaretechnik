package de.teamrocket.relaxo.models.taskcomponent;

/**
 * Repraesentation eines Elements einer Auswahl von TaskComponents
 */
public class TaskComponentSelectionElement {

    // Vars

    /**
     * Der Wert.
     */
    private int value;

    /**
     * Die Bezeichnung.
     */
    private String name;

    // GETTER / SETTER

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}