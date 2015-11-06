package de.teamrocket.relaxo.rest.models.formgroup.response;

import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.rest.models.task.response.TaskComponentResponse;
import de.teamrocket.relaxo.rest.models.task.response.TaskComponentResponseFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Antwort des REST-Servers auf Anfragen nach FormGroupResponses
 */
public class FormGroupResponse {

    /**
     * ID der Gruppe
     */
    private int id;

    /**
     * Title der FormGroup
     */
    private String name;

    /**
     * Liste der TaskComponents einer FormGroup
     */
    private List<TaskComponentResponse> components;

    /**
     * Leerer Konstruktor
     */
    public FormGroupResponse() {
    }

    /**
     * Erstellt eine Response anhand der Gruppe und der Liste der Components
     *
     * @param group die Gruppe der Components
     */
    public FormGroupResponse(FormGroup group, TaskComponentResponseFactory taskComponentResponseFactory) {
        this.name = group.getName();
        this.id = group.getId();
        this.components = new LinkedList<>();

        TaskComponent taskComponent;
        TaskComponentResponse taskComponentResponse;

        if (group.getComponents() != null) {
            for (TaskComponent component : group.getComponents()) {
                taskComponent = component;
                taskComponentResponse = taskComponentResponseFactory.newTaskComponentResponse(taskComponent);
                this.components.add(taskComponentResponse);
            }
        }
    }

    public List<TaskComponentResponse> getComponents() {
        return components;
    }

    public void setComponents(List<TaskComponentResponse> components) {
        this.components = components;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
