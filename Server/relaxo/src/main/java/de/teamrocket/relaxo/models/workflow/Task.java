package de.teamrocket.relaxo.models.workflow;

import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;

import java.util.List;


/**
 * Task ist eine konkrete Ausprägung der Klasse WorkflowItem.
 * Ein Task hält eine Liste von Benutzergruppen und eine Liste von Task-Komponenten.
 * Jeder Task zeigt auf das nachfolgende WorkflowItem.
 */

public class Task extends WorkflowItem {

    /**
     * der Nachfolger dieses Task
     */
    private Integer nextWorkflowItemId;

    /**
     * Liste der TaskComponents
     */
    private List<TaskComponent> taskComponents;

    public Task() {
        this.setType("TASK");
    }

    public Integer getNextWorkflowItemId() {
        return nextWorkflowItemId;
    }

    public void setNextWorkflowItemId(Integer nextWorkflowItemId) {
        this.nextWorkflowItemId = nextWorkflowItemId;
    }

    public List<TaskComponent> getTaskComponents() {
        return taskComponents;
    }

    public void setTaskComponents(List<TaskComponent> taskComponents) {
        this.taskComponents = taskComponents;
    }

}