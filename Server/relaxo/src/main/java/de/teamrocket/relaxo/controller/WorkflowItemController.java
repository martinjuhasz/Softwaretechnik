package de.teamrocket.relaxo.controller;

import de.teamrocket.relaxo.controller.exceptions.*;
import de.teamrocket.relaxo.persistence.exceptions.WorkflowItemConnectionException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;

import java.util.List;

/**
 * Controller Interface fuer WorkflowItems
 */
public interface WorkflowItemController {

    /**
     * Gibt ein neues WorkflowItem fuer den uebergebenen Workflow und mit dem uebergebenen Type zurueck
     *
     * @param workflow der uebergebene Workflow
     * @param type     der uebergebene Type
     * @param user     User, der die Änderung macht
     * @return ein neues WorkflowItem
     */
    public WorkflowItem createWorkflowItem(Workflow workflow, String type, int xPos, int yPos, User user) throws WorkflowStartItemExistException, WorkflowItemTypeException;

    /**
     * Gibt zurück, ob ein Workflow mit der übergebenden id existiert.
     *
     * @param id die id des Workflows
     * @return true, falls dieses exisitert
     */
    public boolean workflowItemExists(int id);

    /**
     * Gibt ein WorkflowItem anhand der ID wieder.
     *
     * @param id die ID des WorkflowItems
     * @return das angefragte WorkflowItem
     */
    public WorkflowItem getWorkflowItemById(int id);

    /**
     * Gibt eine Liste von WorkflowItems zurück, die dem Workflow gehören, dessen id übergeben wurde.
     *
     * @param id die Workflow-ID
     * @return Liste mit WorkflowItems
     */
    public List<WorkflowItem> getWorkflowItemsByWorkflowId(int id);

    /**
     * Entfernt ein Workflow aus dem System, sofern aus dessen Workflow noch keine Jobs instanziiert wurden.
     * Sonst folgt eine Exception.
     *
     * @param workflowItemId WorkflowItem ID des Items, welches entfernt werden soll.
     * @param user User, der die Änderung macht
     */
    public void deleteWorkflowItem(int workflowItemId, User user) throws WorkflowNotEditableException, WorkflowItemNotFoundException;

    /**
     * Ueberprueft ob der uebergebene Type korrekt ist
     *
     * @param type der uebergebene Type
     * @return TRUE wenn der Type korrekt ist, wenn nicht FALSE
     */
    public boolean checkWorkflowItemType(String type);

    /**
     * Gibt die IDs der Usergruppen eines Tasks zurück
     *
     * @param workflowItem der gewünschte Task
     * @return Liste der IDs der Usergruppen
     */
    public List<Integer> getUserGroupsForWorkflowItem(WorkflowItem workflowItem);

    /**
     * Fügt Usergruppen einer Task hinzu
     *
     * @param workflowItem die Task
     * @param groupIds     Liste der IDs aller Usergruppen die hinzugefügt werden solen
     */
    public void setUserGroupsForWorkflowItem(WorkflowItem workflowItem, List<Integer> groupIds);

    /**
     * Gibt eine Liste der WorkflowItems des uebergebenen Jobs zurueck
     *
     * @param job der uebergebene Job
     * @return eine Liste der WorkflowItems des Jobs
     */
    public List<WorkflowItem> getWorkflowItemsByJob(Job job);

    /**
     * Gibt eine Liste der WorkflowItems, mit dem gesuchten Status, des uebergebenen Jobs zurueck
     *
     * @param job    der uebergebene Job
     * @param status der gesuchte Status (true fuer erledigte, false fuer nicht erledigte WorkflowItems)
     * @return eine Liste der WorkflowItems, mit dem gesuchten Status, des Jobs
     */
    public List<WorkflowItem> getWorkflowItemsByJobAndStatus(Job job, String status);

    /**
     * Erstellt enen TaskComponent
     *
     * @param taskComponent die zu erstellende TaskComponent
     * @param user          User, der die Änderung macht
     * @param workflowId    Id des zugehörigen Workflows
     */
    public void createTaskComponent(TaskComponent taskComponent, User user, int workflowId);

    /**
     * Updated die Position des WorkflowItems im grafischen Workflow Editor
     *
     * @param id   des WorkflowItems
     * @param xPos neue x Koordinate
     * @param yPos neue y Koordinate
     * @param user User, der die Änderung macht
     */
    public void updateWorkflowItemPosition(int id, int xPos, int yPos, User user) throws WorkflowItemNotFoundException;

    /**
     * Sperrt ein Workflowitem
     * @param user           User, der die Änderung macht
     * @param workflowItemId die ID des WorkflowItems
     */
    public void lockWorkflowItem(int workflowItemId, User user) throws WorkflowItemLockException, WorkflowItemNotFoundException;

    /**
     * Entsperrt ein Workflowitem
     *
     * @param user           User, der die Änderung macht
     * @param workflowItemId die ID des WorkflowItems
     */
    public void unlockWorkflowItem(int workflowItemId, User user) throws WorkflowItemLockException, WorkflowItemNotFoundException;

    /**
     * Setzt NextItems des WorkflowItems
     *
     * @param workflowItemId      für das die nextItems gesetzt werden sollen
     * @param nextWorkflowItemIds Liste der Ids der WorkflowItems, die nextItems werden sollen
     * @param user                User, der die Änderung macht
     * @throws WorkflowItemConnectionException 
     */
    public void setNextWorkflowItems(int workflowItemId, List<Integer> nextWorkflowItemIds, User user) throws NextWorkflowItemException, WorkflowItemNotFoundException, WorkflowItemConnectionException;
}
