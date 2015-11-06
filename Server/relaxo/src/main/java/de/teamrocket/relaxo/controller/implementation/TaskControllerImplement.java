package de.teamrocket.relaxo.controller.implementation;

import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.TaskController;
import de.teamrocket.relaxo.controller.exceptions.FieldMissingException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.TaskComponentForTask;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.persistence.services.FormGroupService;
import de.teamrocket.relaxo.persistence.services.WorkflowItemService;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import java.util.List;

/**
 * Controller Interface fuer Task Management
 */
public class TaskControllerImplement implements TaskController {

    // Static

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
     * Instanz des FormGroupService.
     */
    private final FormGroupService formGroupService;

    // CONSTRUCT

    @Inject
    public TaskControllerImplement(WorkflowItemService workflowItemService, FormGroupService formGroupService) {
        this.workflowItemService = workflowItemService;
        this.formGroupService = formGroupService;
    }

    // METHODS

    @Override
    public List<Task> getTasks(Workflow workflow, User user) {
        if (user.isAdmin()){
            return workflowItemService.getAllTasks(workflow.getId());
        }
        return workflowItemService.getUserTasks(user.getId(), workflow.getId());
    }

    @Override
    public Task getTask(int taskId) {
        return workflowItemService.getTaskById(taskId);
    }

    @Override
    public boolean isUserAbleToSeeTask(User user, Task task) {
        return user.isAdmin() || workflowItemService.canUserAccessWorkflowItem(task.getId(), user.getId());
    }

    @Override
    public List<Integer> getUserGroupsForTask(Task task) {
        return workflowItemService.getUserGroupForWorkflowItem(task.getId());
    }

    @Override
    public void setUserGroupsForTask(Task task, List<Integer> groupIds) {
        workflowItemService.setUserGroupsForWorkflowItem(task.getId(), groupIds);
        LOGGER.info("Die Usergroup wurden für den Task: "+task.getName()+" mit der ID: "+task.getId()+" gesetzt.");
    }

    @Override
    public List<FormGroup> getFormGroupsForTask(Task task) {
        List<FormGroup> formGroups = formGroupService.getFormGroupsByTask(task.getId());
        for (FormGroup formGroup : formGroups) {
            List<TaskComponent> components = formGroupService.getTaskComponentsForTaskAndFormGroup(task.getId(), formGroup.getId());
            formGroup.setComponents(components);
        }
        return formGroups;
    }

    @Override
    public void updateTask(int taskId, String name, List<Integer> usergroups, List<TaskComponentForTask> taskComponentsForTask) throws WorkflowItemNotFoundException, FieldMissingException {
        if(name.isEmpty()) {
            throw new FieldMissingException("Angabe des Namens fehlt!");
        }
        if(usergroups.isEmpty()) {
            throw new FieldMissingException("Angabe der UserGroups fehlt!");
        }
        if(taskComponentsForTask.isEmpty()) {
            throw new FieldMissingException("Angabe der TaskComponents fehlt!");
        }

        workflowItemService.updateTask(taskId, name, usergroups, taskComponentsForTask);

        LOGGER.info("Task "+taskId+" wurde geupdated.");
    }
}
