package de.teamrocket.relaxo.models.taskcomponent;

import java.util.List;

/**
 * Komponente zur Repraesentation einer Auswahl von Komponenten
 */
public class TaskComponentSelection extends TaskComponent {

    // Vars

    /**
     * Gibt an, ob es Radio-Buttons oder Checkboxen sind.
     */
    private boolean multiple;

    /**
     * Liste der Elemente dieser Componente.
     */
    private List<TaskComponentSelectionElement> elements;

    /**
     * Default-Wert.
     */
    private TaskComponentSelectionElement defaultValue;


    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public List<TaskComponentSelectionElement> getElements() {
        return elements;
    }

    public void setElements(List<TaskComponentSelectionElement> elements) {
        this.elements = elements;
    }

    public TaskComponentSelectionElement getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(TaskComponentSelectionElement defaultValue) {
        this.defaultValue = defaultValue;
    }
}