package de.teamrocket.relaxo.models.taskcomponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse welche die einzelnen TaskComponents zu Gruppen bündelt
 */
public class FormGroup {

    /**
     * der Workflow der Gruppe
     */
    private int workflowId;

    /**
     * Die ID der Gruppe
     */
    private int id;

    /**
     * Der Name der Gruppe
     */
    private String name;

    /**
     * die Liste der TaskComponents
     */
    private List<TaskComponent> components;

    public FormGroup() {
        components = new ArrayList<>();
    }


    public List<TaskComponent> getComponents() {
        return components;
    }

    public void setComponents(List<TaskComponent> components) {
        this.components = components;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + " - Id: " + this.id;
    }
}
