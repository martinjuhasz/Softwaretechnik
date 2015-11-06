package de.teamrocket.relaxo.events.models;

import de.teamrocket.relaxo.models.usermanagement.User;

/**
 * Benachrichtigt über Änderungen der FromGroups eines Workflows
 */
public class FormGroupsUpdateEvent {

    // VARS

    private int workflowId;
    private User user;

    // CONSTRUCT

    public FormGroupsUpdateEvent(int workflowId, User user) {
        this.workflowId = workflowId;
        this.user = user;
    }

    // METHODS

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
