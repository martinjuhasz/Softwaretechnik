package de.teamrocket.relaxo.persistence.mapper;

import java.util.List;

import de.teamrocket.relaxo.models.workflow.*;

import org.apache.ibatis.annotations.Param;

import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;

/**
 * Interface fuer WorkflowItem-Statements an die Datenbank
 */
public interface WorkflowItemMapper {

    public List<Task> getUserTasks(@Param("userId")int userId, @Param("workflowId")int workflowId);
    public List<WorkflowItem> getWorkflowItemsByJob(int jobId);
    public List<WorkflowItem> getWorkflowItemsByJobAndStatus(@Param("jobId")int jobId, @Param("status")String status);
    public boolean canUserAccessWorkflowItem(@Param("workflowItemId")int taskId, @Param("userId")int userId);
    public List<TaskComponent> getTaskComponents(int taskId);
    public void createWorkflowItem(WorkflowItem workflowItem);
    public void updateWorkflowItem(WorkflowItem workflowItem);
    public List<WorkflowItem> getWorkflowItemsByWorkflowId(int workflowId);
    public void deleteWorkflowItem(int workflowItemId);
    public void updatePosition(@Param("workflowItemId")int workflowItemId, @Param("xPos") int xPos, @Param("yPos") int yPos);
    public void setNextWorkflowItems(@Param("workflowItemId")int workflowItemId, @Param("nextWorkflowItemIds")List<Integer> nextWorkflowItemIds);
    public void removeNextWorkflowItems(int workflowItemId);
    public WorkflowItem getWorkflowItemById(int workflowItemId);
    public boolean checkWorkflowItemType(String type);
    public Task getTaskById(int taskId);
    public void lockWorkflowItem(int workflowItemId);
    public void unlockWorkflowItem(int workflowItemId);
    public boolean isWorkflowItemLocked(int workflowId);
    public void setUserGroupsForTask(@Param("workflowItemId")int workflowItemId, @Param("usergroupIds")List<Integer> usergroupIds);
    public void removeUserGroupFromWorkflowItem(@Param("workflowItemId")int workflowItemId, @Param("userGroupId")int userGroupId);
    public void removeAllUserGroupsFromWorkflowItem(int workflowItemId);
    public List<Integer> getUserGroupForWorkflowItem(int workflowItemId);
    public List<FormGroupForTask> getComponentsAndFormGroupsForTask(int workflowItemId);
    public void createTaskComponent(TaskComponent taskComponent);
    public void deleteTaskComponent(int taskComponentId);
    public void setComponentsForTask(@Param("workflowItemId")int workflowItemId, @Param("taskComponentsForTask")List<TaskComponentForTask> taskComponentsForTask);
    public void removeComponentFromWorkflowItem(@Param("workflowItemId")int workflowItemId, @Param("taskComponentId")int taskComponentId);
    public void removeAllComponentsFromWorkflowItem(int workflowItemId);
    public String getTaskComponentType(int taskComponentId);
    public List<Integer> getNextWorkflowItems(int workflowItemId);
    public WorkflowDecision getWorkflowDecisionById(int workflowDecisionId);
    public void setConditionAndNextOnTrueForWorkflowDecision(@Param("workflowDecisionId") int workflowDecisionId, @Param("condition") String condition, @Param("nextWorkflowItemOnTrue") int nextWorkflowItemOnTrue);
    public void removeConditionAndNextOnTrueFromWorkflowDecision(int workflowDecisionId);
    public WorkflowScript getWorkflowScriptById(int workflowScriptId);
    public void setScriptForWorkflowScript(@Param("workflowScriptId") int workflowScriptId, @Param("script") String script);
    public void removeScriptFromWorkflowScript(int workflowScriptId);
    public void setVariablesForWorkflowItem(@Param("workflowItemId") int workflowItemId, @Param("variables") List<TaskVariable> variables);
    public void removeVariablesFromWorkflowItem(int workflowItemId);
    public List<Task> getAllTasks(int workflowId);
    public List<Integer> getPreviousWorkflowItems(int workflowItemId);
}
