package de.teamrocket.relaxo.messaging.beans;

/**
 * Created by martinjuhasz on 18/01/15.
 */
public class MessagingWorkflowsUpdateBean {

    private int userId;
    private int workflowId;

    public MessagingWorkflowsUpdateBean() {
    }

    public MessagingWorkflowsUpdateBean(int workflowId, int userId) {
        this.userId = userId;
        this.workflowId = workflowId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }
}
