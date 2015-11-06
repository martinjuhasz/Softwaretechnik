package de.teamrocket.relaxo.controller.commands;

import de.teamrocket.relaxo.controller.CommandExecutor;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;

/**
 * Das Command, das den Wechsel von einem WorkflowItem zum nächsten implementiert
 */
public class SwitchWorkflowItemCommand implements WorkflowItemCommand {

    // Vars

    private final CommandExecutor commandExecutor;
    private final WorkflowItemCommandFactory workflowItemCommandFactory;
    private final WorkflowItemService workflowItemService;

    private int jobId;
    private int nextWorkflowItemId;
    private WorkflowItem workflowItem;

    // Construct

    public SwitchWorkflowItemCommand(WorkflowItem workflowItem, int nextWorkflowItemId, int jobId, CommandExecutor commandExecutor, WorkflowItemCommandFactory workflowItemCommandFactory, WorkflowItemService workflowItemService) {
        this.nextWorkflowItemId = nextWorkflowItemId;
        this.jobId = jobId;
        this.workflowItem = workflowItem;
        this.commandExecutor = commandExecutor;
        this.workflowItemCommandFactory = workflowItemCommandFactory;
        this.workflowItemService = workflowItemService;
    }

    // Methods

    /**
     * Für das nächste WorkflowItem wird ein EnterCommand erstellt und an den {@link CommandExecutor} gereicht.
     */
    @Override
    public void execute() {
        WorkflowItem nextWorkflowItem = workflowItemService.getWorkflowItemById(nextWorkflowItemId);
        WorkflowItemCommand workflowItemCommand = workflowItemCommandFactory.createWorkflowItemEnterCommand(nextWorkflowItem, workflowItem, jobId);
        commandExecutor.submit(workflowItemCommand);
    }
}
