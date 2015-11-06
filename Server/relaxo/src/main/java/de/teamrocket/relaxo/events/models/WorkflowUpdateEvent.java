package de.teamrocket.relaxo.events.models;

import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;

/**
 * Created by jthei001 on 14.01.2015.
 */
public class WorkflowUpdateEvent {

    // VARS

    /**
     * Der Workflow der aktualisiert wurde
     */
    private Workflow workflow;

    /**
     * Der User, der die Aktualisierung durchgeführt hat
     */
    private User user;

    // CONSTRUCT

    /**
     * Konstruktor zum erstellen eines WorkflowUpdateEvent
     *
     * @param workflow der aktualisierte Workflow
     * @param user
     */
    public WorkflowUpdateEvent(Workflow workflow, User user) {
        this.workflow = workflow;
        this.user = user;
    }

    // METHODS

    public Workflow getWorkflow() {
        return workflow;
    }

    public User getUser() {
        return user;
    }
}
