package de.teamrocket.relaxo.persistence.mapper;

import de.teamrocket.relaxo.models.workflow.Workflow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Interface fuer Workflow-Statements an die Datenbank
 */
public interface WorkflowMapper {

    public void updateWorkflow(Workflow workflow);

    public void createWorkflow(Workflow workflow);

    public Workflow getWorkflowById(int workflowId);

    public List<Workflow> getAllWorkflows();

    public List<Workflow> getEditableWorkflows();

    public List<Workflow> getUserWorkflows(int userId);

    public void deleteWorkflow(int workflowId);

    public void updateRunnableStateForWorkflow(@Param("runnable") boolean runnable, @Param("workflowId") int workflowId);

    public void setStartItemId(@Param("workflowId") int workflowId, @Param("startItemId") int startItemId);

}
