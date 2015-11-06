package de.teamrocket.relaxo.controller.implementation;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.WorkflowItemController;
import de.teamrocket.relaxo.controller.exceptions.*;
import de.teamrocket.relaxo.events.models.FormGroupsUpdateEvent;
import de.teamrocket.relaxo.events.models.WorkflowUpdateEvent;
import de.teamrocket.relaxo.persistence.exceptions.WorkflowItemConnectionException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.*;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import java.util.List;

/**
 * Implementierung des WorkflowItem-Controllers
 */
public class WorkflowItemControllerImplement implements WorkflowItemController {

    // STATIC

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.CONTROLLER);

    // VARS

    /**
     * Instanz des WorkflowItemService.
     */
    private final WorkflowItemService workflowItemService;

    /**
     *  Instanz des Eventsbusses.
     */
    private final EventBus eventBus;

    // CONSTRUCT

    @Inject
    public WorkflowItemControllerImplement(WorkflowItemService workflowItemService, EventBus eventBus) {
        this.workflowItemService = workflowItemService;
        this.eventBus = eventBus;
    }

    // METHODS

    @Override
    public WorkflowItem createWorkflowItem(Workflow workflow, String type, int xPos, int yPos, User user) throws WorkflowStartItemExistException, WorkflowItemTypeException {

        WorkflowItem newWorkflowItem = workflowItemService.createWorkflowItem(workflow, type, xPos, yPos);

        if (user != null) {
            // schicke Event, dass Workflow geupdated wurde (für WorkflowEditor)
            eventBus.post(new WorkflowUpdateEvent(workflow, user));
        }

        return newWorkflowItem;
    }

    @Override
    public boolean workflowItemExists(int id) {
        return workflowItemService.getWorkflowItemById(id) != null;
    }

    @Override
    public WorkflowItem getWorkflowItemById(int id) {
        return workflowItemService.getWorkflowItemById(id);
    }

    @Override
    public List<WorkflowItem> getWorkflowItemsByWorkflowId(int id) {
        return workflowItemService.getWorkflowItemsByWorkflowId(id);
    }

    @Override
    public void deleteWorkflowItem(int workflowItemId, User user) throws WorkflowNotEditableException, WorkflowItemNotFoundException {
        Workflow workflow = workflowItemService.deleteWorkflowItem(workflowItemId);
        LOGGER.info("Das WorkflowItem mit Id: " + workflowItemId + " wurde entfernt.");

        if (user != null) {
            // schicke Event, dass Workflow geupdated wurde (für WorkflowEditor)
            eventBus.post(new WorkflowUpdateEvent(workflow, user));
        }
    }

    @Override
    public List<Integer> getUserGroupsForWorkflowItem(WorkflowItem workflowItem) {
        return workflowItemService.getUserGroupForWorkflowItem(workflowItem.getId());
    }

    @Override
    public void setUserGroupsForWorkflowItem(WorkflowItem workflowItem, List<Integer> groupIds) {
        workflowItemService.setUserGroupsForWorkflowItem(workflowItem.getId(), groupIds);
    }


    @Override
    public boolean checkWorkflowItemType(String type) {
        return workflowItemService.checkWorkflowItemType(type);
    }

    @Override
    public List<WorkflowItem> getWorkflowItemsByJob(Job job) {
        return workflowItemService.getWorkflowItemsByJob(job.getId());
    }

    @Override
    public List<WorkflowItem> getWorkflowItemsByJobAndStatus(Job job, String status) {
        return workflowItemService.getWorkflowItemsByJobAndStatus(job.getId(), status);
    }

    @Override
    public void createTaskComponent(TaskComponent taskComponent, User user, int workflowId) {
        workflowItemService.createTaskComponent(taskComponent);

        // schicke Event, dass Workflow geupdated wurde (für WorkflowEditor)
        eventBus.post(new FormGroupsUpdateEvent(workflowId, user));
    }

    @Override
    public void updateWorkflowItemPosition(int id, int xPos, int yPos, User user) throws WorkflowItemNotFoundException {
        workflowItemService.updateWorkflowItemPosition(id, xPos, yPos);

        if (user != null) {
            WorkflowItem workflowItem = getWorkflowItemById(id);
            Workflow workflow = new Workflow();
            workflow.setId(workflowItem.getWorkflowId());

            // schicke Event, dass Workflow geupdated wurde (für WorkflowEditor)
            eventBus.post(new WorkflowUpdateEvent(workflow, user));
        }

        LOGGER.info("Das WorkflowItem mit der ID: "+id+" wurde verschoben.");
    }

    @Override
    public void lockWorkflowItem(int workflowItemId, User user) throws WorkflowItemLockException, WorkflowItemNotFoundException {
        workflowItemService.lockWorkflowItem(workflowItemId);

        if (user != null) {
            WorkflowItem workflowItem = getWorkflowItemById(workflowItemId);
            Workflow workflow = new Workflow();
            workflow.setId(workflowItem.getWorkflowId());

            // schicke Event, dass Workflow geupdated wurde (für WorkflowEditor)
            eventBus.post(new WorkflowUpdateEvent(workflow, user));
        }
    }

    @Override
    public void unlockWorkflowItem(int workflowItemId, User user) throws WorkflowItemLockException, WorkflowItemNotFoundException {
        workflowItemService.unlockWorkflowItem(workflowItemId);

        if (user != null) {
            WorkflowItem workflowItem = getWorkflowItemById(workflowItemId);
            Workflow workflow = new Workflow();
            workflow.setId(workflowItem.getWorkflowId());

            // schicke Event, dass Workflow geupdated wurde (für WorkflowEditor)
            eventBus.post(new WorkflowUpdateEvent(workflow, user));
        }
    }

    @Override
    public void setNextWorkflowItems(int workflowItemId, List<Integer> nextWorkflowItemIds, User user) throws NextWorkflowItemException, WorkflowItemNotFoundException, WorkflowItemConnectionException {
        workflowItemService.setNextWorkflowItems(workflowItemId, nextWorkflowItemIds);

        if (user != null) {
            WorkflowItem workflowItem = getWorkflowItemById(workflowItemId);
            Workflow workflow = new Workflow();
            workflow.setId(workflowItem.getWorkflowId());

            // schicke Event, dass Workflow geupdated wurde (für WorkflowEditor)
            eventBus.post(new WorkflowUpdateEvent(workflow, user));

            LOGGER.info("Das NextWorkflowItem wurde vom WorkflowItem mit der ID: "+workflowItemId+" wurde gesetzt. ");
        }
    }

}
