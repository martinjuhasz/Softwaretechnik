package de.teamrocket.relaxo.controller.commands;

import com.google.common.eventbus.EventBus;
import de.teamrocket.relaxo.events.models.TaskUpdateEvent;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobUpdateComponent;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.persistence.services.JobService;

import java.util.List;

/**
 * Das Command, das einen {@link Task} updated
 */
public class TaskUpdateCommand implements WorkflowItemCommand {

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
     * Instanz der WorkflowItemCommandFactory zum wechsel des aktuellen JobWorkflowItems.
     */
    private final WorkflowItemCommandFactory workflowItemCommandFactory;

    /**
     * Instanz des Tasks.
     */
    private Task task;

    /**
     * ID des Jobs.
     */
    private int jobId;

    /**
     * Die Daten, die in das JobTask inserted werden sollen.
     */
    private List<JobUpdateComponent> components;

    /**
     * ID des Users, der dieses JobTask updated.
     */
    private int editorUserId;

    // Construct

    public TaskUpdateCommand(Task task, int jobId, List<JobUpdateComponent> components, int editorUserId, JobService jobService, EventBus eventBus, WorkflowItemCommandFactory workflowItemCommandFactory) {
        this.task = task;
        this.jobId = jobId;
        this.components = components;
        this.editorUserId = editorUserId;
        this.jobService = jobService;
        this.eventBus = eventBus;
        this.workflowItemCommandFactory = workflowItemCommandFactory;
    }

    // Methods

    /**
     * Der JobTask wird mit den gesetzten Komponenten geupdatet und ein {@link TaskUpdateEvent} gepostet.
     * Außerdem wird ein {@link SwitchWorkflowItemCommand} für das folgende WorfklowItem erstellt und direkt ausgeführt.
     */
    @Override
    public void execute() {
        jobService.finishJobTask(task, jobId, components, editorUserId);
        eventBus.post(new TaskUpdateEvent(task));
        SwitchWorkflowItemCommand switchWorkflowItemCommand = workflowItemCommandFactory.createSwitchWorkflowItemCommand(task, task.getNextWorkflowItemId(), jobId);
        switchWorkflowItemCommand.execute();
    }

}
