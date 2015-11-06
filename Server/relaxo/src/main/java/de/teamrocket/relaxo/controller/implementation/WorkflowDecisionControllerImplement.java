package de.teamrocket.relaxo.controller.implementation;

import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.WorkflowDecisionController;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;

import java.util.List;

public class WorkflowDecisionControllerImplement implements WorkflowDecisionController {

    // Vars

    /**
     * Instanz des WorkflowItemService.
     */
    private final WorkflowItemService workflowItemService;

    // Constructor

    @Inject
    public WorkflowDecisionControllerImplement(WorkflowItemService workflowItemService) {
        this.workflowItemService = workflowItemService;
    }

    // Methods

    @Override
    public WorkflowDecision getWorkflowDecision(int workflowDecisionId) throws WorkflowItemNotFoundException {
        return workflowItemService.getWorkflowDecisionById(workflowDecisionId);
    }

    @Override
    public void updateWorkflowDecision(int workflowDecisionId, List<TaskVariable> variables, String condition, int nextWorkflowItemOnTrue) throws WorkflowItemNotFoundException {
        //TODO condition und id's prüfen
        workflowItemService.updateWorkflowDecision(workflowDecisionId, variables, condition, nextWorkflowItemOnTrue);
    }

}
