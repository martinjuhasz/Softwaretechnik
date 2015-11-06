package de.teamrocket.relaxo.controller.commands;

import java.util.LinkedList;
import java.util.List;

import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JoinBranch;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.models.workflow.WorkflowJoin;
import de.teamrocket.relaxo.persistence.services.JobService;

/**
 * Das Command, was beim Erreichen eines {@link WorkflowJoin}s im Workflow ausgeführt wird.
 */
public class WorkflowJoinEnterCommand implements WorkflowItemCommand {

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
	 * Instanz des WorkflowJoin-Objects.
	 */
    private WorkflowJoin workflowJoin;

	/**
	 * Instanz des vorrigen WorkflowItem.
	 */
    private WorkflowItem previousWorkflowItem;

	/**
	 * ID des Jobs.
	 */
    private int jobId;

	// Constructor

    public WorkflowJoinEnterCommand(WorkflowJoin workflowJoin, WorkflowItem previousWorkflowItem, int jobId, JobService jobService, WorkflowItemCommandFactory workflowItemCommandFactory) {
    	this.workflowJoin = workflowJoin;
    	this.previousWorkflowItem = previousWorkflowItem;
    	this.jobId = jobId;
    	this.jobService = jobService;
    	this.workflowItemCommandFactory = workflowItemCommandFactory;
    }
    
    /**
     * Erstellt einen neuen {@link JoinBranch} mit dem gesetzten WorkflowJoin und dem vorherigen {@link WorkflowItem} im {@link Job} mit der gesetzten jobId.
     * Wenn bereits zu allen anderen vorherigen {@link WorkflowItem}s des WorkflowJoins JoinBranches (im gleichen Job) erstellt wurden,
     * wird bei alles {@link JoinBranch}es done auf true gesetzt und zum nächsten {@link WorkflowItem} vorgerückt.
     */
	@Override
	public void execute() {
		//neuen JoinBranch erstellen
		JoinBranch joinBranch = new JoinBranch();
		joinBranch.setJobId(jobId);
		joinBranch.setWorkflowItemId(workflowJoin.getId());
		joinBranch.setPrevWorkflowItemId(previousWorkflowItem.getId());
		jobService.createJobWorkflowItem(joinBranch);
		jobService.insertJoinBranch(joinBranch);
		
		List<JoinBranch> waitingJoinBranches = new LinkedList<>();
		
		boolean previousWorkflowItemsDone = true;
		int workflowJoinId = workflowJoin.getId();
		JoinBranch waitingJoinBranch;
		
		//Prüfen, ob zu allen vorherigen WorkflowItems des Joins, ob schon JoinBranches erstellt wurden (die nicht abgeschlossen sind)
		for(Integer previousWorkflowItemId : workflowJoin.getPreviousWorkflowItemIds()){
			if(previousWorkflowItemId != previousWorkflowItem.getId()){
				waitingJoinBranch = jobService.getActiveJoinBranch(jobId, workflowJoinId, previousWorkflowItemId);
				if(waitingJoinBranch == null){
					previousWorkflowItemsDone = false;
					break;
				}
				waitingJoinBranches.add(waitingJoinBranch);
			}
		}
		
		//wenn zu allen WorkflowItems JoinBranches erstellt wurden, die JoinBranches abschließen und zum nächsten WorkflowItem wechseln
		if(previousWorkflowItemsDone){
			jobService.finishJobWorkflowItem(joinBranch.getId());
			for(JoinBranch jb : waitingJoinBranches){
				jobService.finishJobWorkflowItem(jb.getId());
			}
			
	        SwitchWorkflowItemCommand switchWorkflowItemCommand = workflowItemCommandFactory.createSwitchWorkflowItemCommand(workflowJoin, workflowJoin.getNextWorkflowItemId(), jobId);
	        switchWorkflowItemCommand.execute();
		}
	}

}
