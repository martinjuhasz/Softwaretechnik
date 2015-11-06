package de.teamrocket.relaxo.persistence.services;

import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.exceptions.WorkflowLockException;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.persistence.mapper.WorkflowItemMapper;
import de.teamrocket.relaxo.persistence.mapper.WorkflowMapper;
import org.mybatis.guice.transactional.Transactional;

import java.util.List;

/**
 * Service-Klasse zum Persistieren der Workflow Daten
 */
public class WorkflowService {
    /**
     * WorkflowMapper-Interface
     */
    @Inject
    WorkflowMapper workflowMapper;

    /**
     * WorkflowItemMapper-Interface
     */
    @Inject
    WorkflowItemMapper workflowItemMapper;

    /**
     * Erstellt einen leeren Workflow und gibt diesen
     * mit ID zurück
     *
     * @param workflow Objekt, welches mit ID befüllt wird
     */
    @Transactional
    public void createWorkflow(Workflow workflow) {
        workflowMapper.createWorkflow(workflow);
    }

    /**
     * Editiert bestehenden Workflow
     *
     * @param workflow welcher geupdated wird
     */
    @Transactional
    public void updateWorkflow(Workflow workflow) {
        workflowMapper.updateWorkflow(workflow);
    }

    /**
     * Liefert ein Workflow Objekt zurück
     *
     * @param id des gewünschten Workflows
     * @return gewünschter Workflow
     */
    @Transactional
    public Workflow getWorkflowById(int id) {
        return workflowMapper.getWorkflowById(id);
    }

    /**
     * Liefert eine Liste aller Workflows
     *
     * @return alle Workflows
     */
    @Transactional
    public List<Workflow> getAllWorkflows() {
        return workflowMapper.getAllWorkflows();
    }

    /**
     * Gibt alle Workflows zurück die noch nicht laufen und somit bearbeitbar sind.
     *
     * @return Liste der Workflows
     */
    public List<Workflow> getEditableWorkflows() {
        return workflowMapper.getEditableWorkflows();
    }

    /**
     * Gibt alle Workflows zurück, die ein bestimmter User
     * berechtigt ist zu sehen, bzw. in dem er Zugriff auf
     * Workflow-Items hat
     *
     * @param id des Users, dessen Workflows angefragt werden
     * @return Liste der Workflows
     */
    @Transactional
    public List<Workflow> getUserWorkflows(int id) {
        return workflowMapper.getUserWorkflows(id);
    }

    /**
     * Loeschen eines Workflow aus der Datenbank
     * @param id die uebergebene ID des Workflows
     */
    @Transactional
    public void deleteWorkflow(int id) {
        workflowMapper.deleteWorkflow(id);
    }

    /**
     * Aendert das runnable Flag eines Workflows
     *
     * @param runnable   Wert des Flags true/false
     * @param workflowId die Id des Workflows
     */
    @Transactional
    public void updateRunnableStateForWorkflow(boolean runnable, int workflowId) throws WorkflowLockException {
        if(runnable == true){
            List<WorkflowItem> workflowItems = workflowItemMapper.getWorkflowItemsByWorkflowId(workflowId);
            if(workflowItems.isEmpty()){
                throw new WorkflowLockException("Workflow besitzt keine Workflowitems");
            }
            for(WorkflowItem wfi : workflowItems){
                if(wfi.getType().equals("TASK") || wfi.getType().equals("START")) {
                    List<Integer> userGroups = workflowItemMapper.getUserGroupForWorkflowItem(wfi.getId());
                    if (userGroups.isEmpty()) {
                        throw new WorkflowLockException("Jedem Workflowitem muss mindestens eine Usergroup zugeordnet sein");
                    }
                }
            }
        }
        workflowMapper.updateRunnableStateForWorkflow(runnable, workflowId);
    }

    /**
     * Setzt die Start Item ID für einen Workflow
     * @param workflowId des Workflows, welcher upgedated wird
     * @param startItemId des WorkflowItems welches das Start Item wird
     */
    @Transactional
    public void setStartItemId(int workflowId, Integer startItemId){
        workflowMapper.setStartItemId(workflowId, startItemId);
    }

}
