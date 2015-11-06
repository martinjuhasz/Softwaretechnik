package de.teamrocket.relaxo.events.models;


import de.teamrocket.relaxo.models.workflow.Task;

/**
 * Ein Event des Eventbusses welches eine aktualisierung des Tasks indiziert.
 */
public class TaskUpdateEvent {

    // VARS

    /**
     * Der Task der aktualisiert wurde
     */
    private Task task;

    // CONSTRUCT

    /**
     * Konstruktor zum erstellen eines TaskUpdateEvents
     *
     * @param task der aktualisierte Task
     */
    public TaskUpdateEvent(Task task) {
        this.task = task;
    }

    // METHODS

    public Task getTask() {
        return task;
    }
}
