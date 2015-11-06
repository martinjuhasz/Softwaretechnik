package de.teamrocket.relaxo.events.models;

import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;

/**
 * Event welches gefeuert wird wenn ein neuer Workflow hinzugefügt wurde
 */
public class WorkflowsUpdateEvent {

    // VARS

    private Workflow workflow;
    private User user;

    // CONSTRUCT

    public WorkflowsUpdateEvent(Workflow workflow, User user) {
        this.workflow = workflow;
        this.user = user;
    }

    // METHODS

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
