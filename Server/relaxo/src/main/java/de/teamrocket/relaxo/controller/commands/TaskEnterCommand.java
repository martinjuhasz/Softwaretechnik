package de.teamrocket.relaxo.controller.commands;

import com.google.common.eventbus.EventBus;

import de.teamrocket.relaxo.events.models.TaskUpdateEvent;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.persistence.services.JobService;

/**
 * Das Command, was beim Erreichen eines {@link Task}s ausgeführt wird
 */
public class TaskEnterCommand implements WorkflowItemCommand {

    // Vars

    /**
     * Instanz des JobServices.
     */
    private final JobService jobService;

    /**
     * Instanz des EventBus's.
     */
    private final EventBus eventBus;

    /**
     * Instanz des Tasks.
     */
    private Task task;

    /**
     * ID des Jobs.
     */
    private int jobId;

    // Construct

    public TaskEnterCommand(Task task, int jobId, JobService jobService, EventBus eventBus) {
        this.task = task;
        this.jobId = jobId;
        this.jobService = jobService;
        this.eventBus = eventBus;
    }

    // Methods

    /**
     * Für den Task und den aktuellen Job wird ein {@link JobTask} erstellt ein {@link TaskUpdateEvent} gepostet.
     */
    @Override
    public void execute() {
        jobService.createJobTask(task, jobId);

        eventBus.post(new TaskUpdateEvent(task));
    }

}
