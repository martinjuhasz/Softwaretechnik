package de.teamrocket.relaxo.rest.models.workflow.response;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cengizmardin on 09.12.14.
 */
public class WorkflowGetByIdResponse {

    //

    private int id;
    private String name;
    private List<WorkflowItemDetailsResponse> workflowItems = new LinkedList<>();

    //

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

    public List<WorkflowItemDetailsResponse> getWorkflowItems() {
        return workflowItems;
    }

    public void setWorkflowItems(List<WorkflowItemDetailsResponse> workflowItems) {
        this.workflowItems = workflowItems;
    }

    public void addWorkflowItems(WorkflowItemDetailsResponse workflowItem) {
        this.workflowItems.add(workflowItem);
    }
}
