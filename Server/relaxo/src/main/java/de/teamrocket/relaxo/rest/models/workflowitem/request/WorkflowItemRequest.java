package de.teamrocket.relaxo.rest.models.workflowitem.request;


import java.util.List;

/**
 * Created by cengizmardin on 05.12.14.
 */
public class WorkflowItemRequest {

    // Vars

    private String type;
    private WorkflowItemPositionRequest position;
    private List<Integer> nextItem;

    // Setter

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter

    public WorkflowItemPositionRequest getPosition() {
        return position;
    }

    public void setPosition(WorkflowItemPositionRequest position) {
        this.position = position;
    }

    public List<Integer> getNextItem() {
        return nextItem;
    }

    public void setNextItem(List<Integer> nextItem) {
        this.nextItem = nextItem;
    }
}