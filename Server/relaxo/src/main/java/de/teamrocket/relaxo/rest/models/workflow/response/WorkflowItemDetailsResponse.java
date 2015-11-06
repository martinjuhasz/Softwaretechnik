package de.teamrocket.relaxo.rest.models.workflow.response;

import de.teamrocket.relaxo.models.workflow.Start;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;
import de.teamrocket.relaxo.models.workflow.WorkflowFork;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.models.workflow.WorkflowJoin;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cengizmardin on 09.12.14.
 */
public class WorkflowItemDetailsResponse {

    //

    private int id;
    private String type;
    private PositionResponse position;
    private boolean lock;
    private String name;

    private List<Integer> nextItems;

    //

    public WorkflowItemDetailsResponse(WorkflowItem item) {
        this.id = item.getId();
        this.type = item.getType();
        this.position = new PositionResponse(item.getxPos(), item.getyPos());
        this.lock = item.isLocked();
        this.name = item.getName();

        // fülle nextItem Liste
        this.nextItems = new LinkedList<>();
        if (item instanceof Start) {
            Integer nextItem = ((Start) item).getNextWorkflowItemId();
            if (nextItem != null) {
                this.nextItems.add(nextItem);
            }
        } else if (item instanceof Task) {
            Integer nextItem = ((Task) item).getNextWorkflowItemId();
            if (nextItem != null) {
                this.nextItems.add(nextItem);
            }
        } else if (item instanceof WorkflowDecision) {
            this.nextItems = ((WorkflowDecision) item).getNextWorkflowItems();
        } else if (item instanceof WorkflowScript){
        	Integer nextItem = ((WorkflowScript) item).getNextWorkflowItemId();
            if (nextItem != null) {
                this.nextItems.add(nextItem);
            }
        } else if (item instanceof WorkflowFork) {
            this.nextItems = ((WorkflowFork) item).getNextWorkflowItems();
        } else if (item instanceof WorkflowJoin) {
        	Integer nextItem = ((WorkflowJoin) item).getNextWorkflowItemId();
            if (nextItem != null) {
                this.nextItems.add(nextItem);
            }	
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PositionResponse getPosition() {
        return position;
    }

    public void setPosition(PositionResponse position) {
        this.position = position;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public List<Integer> getNextItem() {
        return nextItems;
    }

    public void setNextItem(List<Integer> nextItems) {
        this.nextItems = nextItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
