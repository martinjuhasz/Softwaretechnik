package de.teamrocket.relaxo.controller.commands;

import de.teamrocket.relaxo.models.job.JobWorkflowItem;
import de.teamrocket.relaxo.models.workflow.WorkflowFork;
import de.teamrocket.relaxo.persistence.services.JobService;

/**
 * Das Command, das beim Erreichen eines {@link WorkflowFork}s im Workflow ausgeführt wird.
 */
public class WorkflowForkEnterCommand implements WorkflowItemCommand{

    // Vars

    /**
     * Instanz des JobServices.
     */
    private final JobService jobService;

    /**
     * Instanz der WorkflowItemCommandFactory zum wechsel des aktuellen JobWorkflowItems.
     */
    private final WorkflowItemCommandFactory workflowItemCommandFactory;

    /**
     * Instanz des WorkflowFork-Objects.
     */
    private WorkflowFork workflowFork;

    /**
     * ID des Jobs.
     */
    private int jobId;

    // Construct
    
    public WorkflowForkEnterCommand(WorkflowFork workflowFork, int jobId, JobService jobService, WorkflowItemCommandFactory workflowItemCommandFactory) {
    	this.workflowFork = workflowFork;
    	this.jobId = jobId;
    	this.jobService = jobService;
    	this.workflowItemCommandFactory = workflowItemCommandFactory;
    }

    // Methods

    /**
     * Zu der Fork wird ein {@link JobWorkflowItem} erstellt und für jedes nextWorkflowItem des Forks
     * wird ein {@link SwitchWorkflowItemCommand} erstellt und ausgeführt.
     */
	@Override
	public void execute() {
        JobWorkflowItem jobWorkflowItem = new JobWorkflowItem();
        jobWorkflowItem.setJobId(jobId);
        jobWorkflowItem.setWorkflowItemId(workflowFork.getId());
        jobService.createJobWorkflowItem(jobWorkflowItem);
        
        jobService.finishJobWorkflowItem(jobWorkflowItem.getId());
        
        for(int nextWorkflowItemId : workflowFork.getNextWorkflowItems()){
            SwitchWorkflowItemCommand switchWorkflowItemCommand = workflowItemCommandFactory.createSwitchWorkflowItemCommand(workflowFork, nextWorkflowItemId, jobId);
            switchWorkflowItemCommand.execute();
        }
	}
}
