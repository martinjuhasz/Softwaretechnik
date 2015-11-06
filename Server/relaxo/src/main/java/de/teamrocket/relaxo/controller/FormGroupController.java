package de.teamrocket.relaxo.controller;

import de.teamrocket.relaxo.models.formgroup.TaskComponentOrder;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;

import java.util.List;

/**
 * Controller Interface fuer FormGroup
 */
public interface FormGroupController {

    /**
     * Gibt eine Liste aller FormGroups eines Workflows zurück
     *
     * @param workflow Workflow, dessen FormGroups abgefragt werden
     * @return Liste der FormGroups
     */
    public List<FormGroup> getFormGroupsForWorkflow(Workflow workflow);

    /**
     * Erstellt eine neue FormGroup
     *
     * @param workflowId ID des Workflows
     * @param groupName  Name der Gruppe
     * @param user       der User welcher die FormGroup erstellt hat
     * @return die erstellte FormGroup
     */
    public FormGroup createFormGroup(int workflowId, String groupName, User user);

    /**
     * Gibt eine FormGroup anhand einer Id zurück
     *
     * @param formGroupId ID der FormGroup, welche zurückgegeben werden soll
     * @return die FormGroup
     */
    public FormGroup getFormGroupById(int formGroupId);

    /**
     * Updated die Order von Components
     *
     * @param formGroupId ID der zugehörigen FormGroup
     * @param components  Liste der Components
     * @param user        der User welcher die FormGroup erstellt hat
     */
    public void updateComponentsOrder(int formGroupId, List<TaskComponentOrder> components, User user);
}
