package de.teamrocket.relaxo.controller.commands;

import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.CommandExecutor;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobUpdateComponent;
import de.teamrocket.relaxo.models.workflow.End;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;
import de.teamrocket.relaxo.models.workflow.WorkflowFork;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.models.workflow.WorkflowJoin;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;
import de.teamrocket.relaxo.persistence.services.JobService;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;
import de.teamrocket.relaxo.util.ThreadSafePythonInterpreter;

/**
 * Factory zum Erstellen von {@link WorkflowItemCommand}s.
 */
public class WorkflowItemCommandFactory {

    // Vars

    /**
     * Instanz des CommandExecutor.
     */
    private final CommandExecutor commandExecutor;

    /**
     * Instanz des JobServices.
     */
    private final JobService jobService;

    /**
     * Instanz des WorkflowItemService.
     */
    private final WorkflowItemService workflowItemService;

    /**
     * Instanz des EventBus.
     */
    private final EventBus eventBus;

    /**
     * Der Python-Interpreter zum ausführen des Python-Codes.
     */
    private final ThreadSafePythonInterpreter interpreter;

    // Construct

    @Inject
    public WorkflowItemCommandFactory(CommandExecutor commandExecutor, JobService jobService, WorkflowItemService workflowItemService, EventBus eventBus, ThreadSafePythonInterpreter interpreter) {
        this.commandExecutor = commandExecutor;
        this.jobService = jobService;
        this.workflowItemService = workflowItemService;
        this.eventBus = eventBus;
        this.interpreter = interpreter;
    }

    // Methods

    /**
     * Erstellt je nach übergebenem WorkflowItem das passende EnterCommand.
     * @param workflowItem das WorkflowItem, zu dem ein EnterCommand erstellt werden soll
     * @param previousWorkflowItem der Vorgänger des WorkflowItems
     * @param jobId die Id des aktuellen Jobs
     * @return das passende EnterCommand
     */
    public WorkflowItemCommand createWorkflowItemEnterCommand(WorkflowItem workflowItem, WorkflowItem previousWorkflowItem, int jobId) {
        WorkflowItemCommand workflowItemCommand = null;

        if (workflowItem instanceof End) {
            workflowItemCommand = createEndEnterCommand(jobId);
        } else if (workflowItem instanceof Task) {
            workflowItemCommand = createTaskEnterCommand((Task) workflowItem, jobId);
        } else if (workflowItem instanceof WorkflowDecision) {
            workflowItemCommand = createWorkflowDecisionEnterCommand((WorkflowDecision) workflowItem, jobId);
        } else if (workflowItem instanceof WorkflowScript) {
        	workflowItemCommand = createWorkflowScriptEnterCommand((WorkflowScript) workflowItem, jobId);
        } else if (workflowItem instanceof WorkflowFork) {
        	workflowItemCommand = createWorkflowForkEnterCommand((WorkflowFork) workflowItem, jobId);
        } else if (workflowItem instanceof WorkflowJoin) {
        	workflowItemCommand = createWorkflowJoinEnterCommand((WorkflowJoin)workflowItem, previousWorkflowItem, jobId);
        }

        return workflowItemCommand;
    }

    /**
     *
     * @param workflowFork Instanz vorrigen workflowFork
     * @param jobId ID des Jobs
     * @return das WorkflowForkEnterCommand-Objekt.
     */
    private WorkflowForkEnterCommand createWorkflowForkEnterCommand(WorkflowFork workflowFork, int jobId){
    	return new WorkflowForkEnterCommand(workflowFork, jobId, jobService, this);
    }

    /**
     *
     * @param workflowJoin Instanz des WorkflowJoin
     * @param previousWorkflowItem Instanz vorrigen WorkflowItems
     * @param jobId ID des Jobs
     * @return das WorkflowJoinEnterCommand-Objekt.
     */
    private WorkflowJoinEnterCommand createWorkflowJoinEnterCommand(WorkflowJoin workflowJoin, WorkflowItem previousWorkflowItem, int jobId){
    	return new WorkflowJoinEnterCommand(workflowJoin, previousWorkflowItem, jobId, jobService, this);
    }

    /**
     *
     * @param workflowDecision Instanz des WorkflowDecision
     * @param jobId ID des Jobs
     * @return das WorkflowDecisionEnterCommand-Objekt.
     */
    private WorkflowDecisionEnterCommand createWorkflowDecisionEnterCommand(WorkflowDecision workflowDecision, int jobId) {
        return new WorkflowDecisionEnterCommand(workflowDecision, jobId, jobService, this, interpreter);
    }

    /**
     *
     * @param workflowScript Instanz des WorkflowScript
     * @param jobId ID des Jobs
     * @return das WorkflowScriptEnterCommand-Objekt.
     */
    private WorkflowScriptEnterCommand createWorkflowScriptEnterCommand(WorkflowScript workflowScript, int jobId) {
        return new WorkflowScriptEnterCommand(workflowScript, jobId, jobService, this, interpreter);
    }

    private EndEnterCommand createEndEnterCommand(int jobId) {
        return new EndEnterCommand(jobId, jobService);
    }

    private TaskEnterCommand createTaskEnterCommand(Task task, int jobId) {
        return new TaskEnterCommand(task, jobId, jobService, eventBus);
    }

    /**
     * Erstellt ein {@link WorkflowItemCommand}, das den übergebenen Task mit den übergebenen Komponentent updatet.
     * @param task der Task, der geupdatet werden soll
     * @param jobId die Id es aktuellen Jobs
     * @param components die Komponenten, mit denen der Job geupdatet werden soll
     * @param editorUserId die Id des Users, der den Task updatet
     * @return das {@link TaskUpdateCommand} zum updaten des Tasks im Job
     */
    public TaskUpdateCommand createTaskUpdateCommand(Task task, int jobId, List<JobUpdateComponent> components, int editorUserId) {
        return new TaskUpdateCommand(task, jobId, components, editorUserId, jobService, eventBus, this);
    }

    /**
     * Erstellt ein {@link WorkflowItemCommand}, um zum nächsten WorkflowItem zu wechseln.
     * @param workflowItem das WorkflowItem, von dem weitergerückt wird
     * @param nextWorkflowItemId die Id des WorkflowItems, zu dem gewechselt werden soll
     * @param jobId die Id des aktuellen Jobs
     * @return das {@link SwitchWorkflowItemCommand} zum weiterrücken des Jobs zum nächsten WorkflowItem
     */
    public SwitchWorkflowItemCommand createSwitchWorkflowItemCommand(WorkflowItem workflowItem, int nextWorkflowItemId, int jobId) {
        return new SwitchWorkflowItemCommand(workflowItem, nextWorkflowItemId, jobId, commandExecutor, this, workflowItemService);
    }
}
