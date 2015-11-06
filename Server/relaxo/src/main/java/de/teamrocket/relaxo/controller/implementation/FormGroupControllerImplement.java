package de.teamrocket.relaxo.controller.implementation;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.FormGroupController;
import de.teamrocket.relaxo.events.models.FormGroupsUpdateEvent;
import de.teamrocket.relaxo.models.formgroup.TaskComponentOrder;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.persistence.services.FormGroupService;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import java.util.List;

/**
 * FormGroupControllerImplement ist eine konkrete Ausprägung des Interface FormGroupController
 */
public class FormGroupControllerImplement implements FormGroupController {

    // STATIC

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.CONTROLLER);

    // VARS

    /**
     * Instanz des FormGroupServices.
     */
    private final FormGroupService formGroupService;

    /**
     *  Instanz des Eventsbusses.
     */
    private final EventBus eventBus;

    // CONTRUCT

    @Inject
    public FormGroupControllerImplement(FormGroupService formGroupService, EventBus eventBus) {
        this.formGroupService = formGroupService;
        this.eventBus = eventBus;
    }

    // METHODS

    @Override
    public List<FormGroup> getFormGroupsForWorkflow(Workflow workflow) {
        return formGroupService.getAllFormGroups(workflow.getId());
    }

    @Override
    public FormGroup createFormGroup(int workflowId, String groupName, User user) {
        FormGroup formGroup = new FormGroup();
        formGroup.setWorkflowId(workflowId);
        formGroup.setName(groupName);
        formGroupService.createFormGroup(formGroup);

        // post event update
        eventBus.post(new FormGroupsUpdateEvent(workflowId, user));

        LOGGER.info("Einer FormGroup wurde für das Workflow "+workflowId+" von "+user.getName()+" mit dem Namen "+groupName+" erstellt.");

        return formGroup;
    }

    @Override
    public FormGroup getFormGroupById(int formGroupId) {
        return this.formGroupService.getFormGroupById(formGroupId);
    }

    @Override
    public void updateComponentsOrder(int formGroupId, List<TaskComponentOrder> components, User user) {

        this.formGroupService.updateComponentsOrder(components);

        // post event update
        FormGroup formGroup = this.formGroupService.getFormGroupById(formGroupId);
        eventBus.post(new FormGroupsUpdateEvent(formGroup.getWorkflowId(), user));

        LOGGER.info("Die FormGroup mit der ID: "+formGroupId+" wurde neu sortiert.");
    }
}
