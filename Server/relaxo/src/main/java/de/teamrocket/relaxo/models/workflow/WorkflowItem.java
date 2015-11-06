package de.teamrocket.relaxo.models.workflow;


/**
 * Ein WorkflowItem stellt die einzelnen Elemente des Workflows dar.
 * WorkflowItems können verschiedene Ausprägungen annehmen, z.B. Task.
 */
public abstract class WorkflowItem {

    // Static

    /**
     * Typ des WorkflowItems
     */
    private String type;

    /**
     * Die x-Position des WorkflowItems.
     */
    private int xPos;

    /**
     * Die y-Position des WorkflowItems.
     */
    private int yPos;

    /**
     * der verweisende Workflow
     */
    private int workflowId;

    /**
     * ID des WorkflowItems
     */
    private int id;

    /**
     * der Name des Tasks
     */
    private String name;

    /**
     * Kommentar zum WorkflowItem.
     */
    private String comment;

    private boolean locked;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}