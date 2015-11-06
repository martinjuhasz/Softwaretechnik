package de.teamrocket.relaxo.controller.implementation;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.WorkflowController;
import de.teamrocket.relaxo.controller.exceptions.WorkflowLockException;
import de.teamrocket.relaxo.events.models.WorkflowsUpdateEvent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.persistence.services.JobService;
import de.teamrocket.relaxo.persistence.services.WorkflowService;

import java.util.List;

/**
 * WorkflowControllerImplement ist eine konkrete Ausprägung des Interface WorkflowController
 */
public class WorkflowControllerImplement implements WorkflowController {

    // VARS

    /**
     * Instanz des WorkflowService.
     */
    private final WorkflowService workflowService;

    /**
     * Instanz des JobService.
     */
    private final JobService jobService;

    /**
     *  Instanz des Eventsbusses.
     */
    private final EventBus eventBus;

    // CONSTRUCT

    @Inject
    public WorkflowControllerImplement(WorkflowService workflowService, JobService jobService, EventBus eventBus) {
        this.workflowService = workflowService;
        this.jobService = jobService;
        this.eventBus = eventBus;
    }

    // METHODS

    @Override
    public Workflow createWorkflow(User user, String name) {
        Workflow workflow = new Workflow();
        workflow.setCreatorId(user.getId());
        workflow.setName(name);
        workflowService.createWorkflow(workflow);

        // post event update
        eventBus.post(new WorkflowsUpdateEvent(workflow, user));

        return workflow;
    }

    @Override
    public void updateWorkflow(Workflow workflow) {
        workflowService.updateWorkflow(workflow);
    }

    @Override
    public Workflow getWorkflowById(int id) {
        return workflowService.getWorkflowById(id);
    }

    @Override
    public List<Workflow> getWorkflowsForUser(User user) {
        if (user.isAdmin()) {
            return workflowService.getEditableWorkflows();
        } else {
            return workflowService.getUserWorkflows(user.getId());
        }

    }

    @Override
    public List<Workflow> getAllWorkflows() {
        return workflowService.getAllWorkflows();
    }

    @Override
    public void deleteWorkflow(Workflow workflow) {
        workflowService.deleteWorkflow(workflow.getId());
    }

    @Override
    public boolean hasActiveJobs(Workflow workflow) {
        if (!jobService.getJobsForWorkflow(workflow).isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserAbleToSeeWorkflow(Workflow workflow, User user) {

        /**
         * Ist der Workflow in der Liste der UserWorkflows?
         */
        List<Workflow> workflows = workflowService.getUserWorkflows(user.getId());
        for (Workflow wf : workflows) {
            if (wf.getId() == workflow.getId()) {
                return true;
            }
        }

        /**
         * Ist der User der Erstelle des Workflow?
         */
        if (workflow.getCreatorId() == user.getId()) {
            return true;
        }

        /**
         * Ist der User ein Admin?
         */
        if (user.isAdmin()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserAbleToCreateAnWorkflow(User user) {
        return user.isAdmin();
    }

    @Override
    public boolean workflowExists(int workflowId) {
        return this.getWorkflowById(workflowId) != null;
    }

    public void updateRunnableStateForWorkflow(Workflow workflow, boolean runnable, User user) throws WorkflowLockException {
        workflowService.updateRunnableStateForWorkflow(runnable, workflow.getId());

        // post event update
        eventBus.post(new WorkflowsUpdateEvent(workflow, user));
    }

}
