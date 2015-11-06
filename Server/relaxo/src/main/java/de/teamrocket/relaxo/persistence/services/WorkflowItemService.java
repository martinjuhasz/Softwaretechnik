package de.teamrocket.relaxo.persistence.services;

import java.util.List;

import org.mybatis.guice.transactional.Transactional;

import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.exceptions.NextWorkflowItemException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemLockException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemTypeException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowNotEditableException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowStartItemExistException;
import de.teamrocket.relaxo.persistence.exceptions.WorkflowItemConnectionException;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.workflow.End;
import de.teamrocket.relaxo.models.workflow.FormGroupForTask;
import de.teamrocket.relaxo.models.workflow.Start;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.TaskComponentForTask;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.models.workflow.WorkflowJoin;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;
import de.teamrocket.relaxo.persistence.mapper.JobMapper;
import de.teamrocket.relaxo.persistence.mapper.WorkflowItemMapper;
import de.teamrocket.relaxo.persistence.mapper.WorkflowMapper;

/**
 * Service-Klasse zum Persistieren von WorkflowItem Daten
 */
public class WorkflowItemService {
	/**
	 * WorkflowItemMapper-Interface
	 */
	@Inject WorkflowItemMapper workflowItemMapper;

	/**
	 * WorkflowMapper-Interface
	 */
	@Inject	WorkflowMapper workflowMapper;

	/**
	 * JobMapper-Interface
	 */
	@Inject	JobMapper jobMapper;

	/**
	 * Gibt eine Liste von Tasks selektiert nach User und Workflow zurueck
	 * @param userId die uerbergebene ID des Users
	 * @param workflowId die uebergebene ID das Workflows
	 * @return Liste von Tasks selektiert nach User und Workflow
	 */
	@Transactional
	public List<Task> getUserTasks(int userId, int workflowId){
		return workflowItemMapper.getUserTasks(userId, workflowId);
	}

	/**
	 * Gibt eine Liste von WorkflowItems fuer einen Job zurueck
	 * @param jobId die uebergebene ID des Job
	 * @return die Liste von WorkflowItems eines Jobs
	 */
	@Transactional
	public List<WorkflowItem> getWorkflowItemsByJob(int jobId) {
		return workflowItemMapper.getWorkflowItemsByJob(jobId);
	}

	/**
	 * Gibt eine Liste von WorkflowItems selektiert nach Job uns Status zurueck
	 * @param jobId die uebergebene ID des Job
	 * @param status der uebergebene Status
	 * @return die Liste von WorkflowItems selektiert nach Job und Status
	 */
	@Transactional
	public List<WorkflowItem> getWorkflowItemsByJobAndStatus(int jobId, String status) {
		return workflowItemMapper.getWorkflowItemsByJobAndStatus(jobId, status);
	}

	/**
	 * Gibt zurueck ob ein User ein WorkflowItem bearbeiten kann
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @param userId die uebergebene ID des User
	 * @return true wenn User das WorkflowItem bearbeiten kann, sonst false
	 */
	@Transactional
	public boolean canUserAccessWorkflowItem(int workflowItemId, int userId){
		return workflowItemMapper.canUserAccessWorkflowItem(workflowItemId, userId);
	}

	/**
	 * Gibt eine Liste von TaskComponents fuer einen Task zurueck
	 * @param taskId die uebergebene ID des Task
	 * @return die Liste von TaskComponents eines Task
	 */
	@Transactional
	public List<TaskComponent> getTaskComponents(int taskId){
		return workflowItemMapper.getTaskComponents(taskId);
	}

	/**
	 * Erstellen eines WorkflowItems in der Datenbank
	 * @param workflowItem das uebergebene WorkflowItem
	 */
	@Transactional
	public void createWorkflowItem(WorkflowItem workflowItem){
		workflowItemMapper.createWorkflowItem(workflowItem);
	}

	/**
	 * Erstellen eines WorkflowItems in der Datenbank
	 * @param workflow der uebergebene Workflow
	 * @param type der uebergebene Typ des WorkflowItems
	 * @param xPos die uebergebene X-Position des WorkflowItems
	 * @param yPos die uebergebene Y-Position des WorkflowItems
	 * @return das neu erstellte WorkflowItem
	 * @throws WorkflowStartItemExistException
	 * @throws WorkflowItemTypeException
	 */
	@Transactional
	public WorkflowItem createWorkflowItem(Workflow workflow, String type, int xPos, int yPos) throws WorkflowStartItemExistException, WorkflowItemTypeException {
		WorkflowItem newWorkflowItem;
		switch (type) {
			case "START":
				if(workflow.getStartItemId() != null){
					throw new WorkflowStartItemExistException();
				}
				newWorkflowItem = new Start();
				break;
			case "END":
				newWorkflowItem = new End();
				break;
			case "TASK":
				newWorkflowItem = new Task();
				break;
			case "DECISION":
				newWorkflowItem = new WorkflowDecision();
				break;
			case "SCRIPT":
				newWorkflowItem = new WorkflowScript();
				break;
			case "FORK":
				newWorkflowItem = new WorkflowScript();
				break;
			case "JOIN":
				newWorkflowItem = new WorkflowJoin();
				break;
			default:
				throw new WorkflowItemTypeException();
		}
		newWorkflowItem.setWorkflowId(workflow.getId());
		newWorkflowItem.setType(type);
		newWorkflowItem.setxPos(xPos);
		newWorkflowItem.setyPos(yPos);
		workflowItemMapper.createWorkflowItem(newWorkflowItem);
		if(newWorkflowItem.getType().equals("START")){
			workflowMapper.setStartItemId(workflow.getId(), newWorkflowItem.getId());
		}
		return newWorkflowItem;
	}

	/**
	 * Update eines WorkflowItems in der Datenbank
	 * @param workflowItem das uebergebene WorkflowItem
	 */
	@Transactional
	public void updateWorkflowItem(WorkflowItem workflowItem){
		workflowItemMapper.updateWorkflowItem(workflowItem);
	}

	/**
	 * Update der Position eines WorkflowItems
	 * @param id die uebergebene ID des WorkflowItems
	 * @param xPos die uebergebene X-Position des WorkflowItems
	 * @param yPos die uebergebene Y-Position des WorkflowItems
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public void updateWorkflowItemPosition(int id, int xPos, int yPos) throws WorkflowItemNotFoundException {
		if(workflowItemMapper.getWorkflowItemById(id) != null){
			workflowItemMapper.updatePosition(id, xPos, yPos);
		} else {
			throw new WorkflowItemNotFoundException();
		}
	}

	/**
	 * Gibt eine Liste von WorkflowItems fuer einen Workflow zurueck
	 * @param workflowId die uebergebene ID das Workflows
	 * @return die Liste von WorkflowItems fuer einen Workflow
	 */
	@Transactional
	public List<WorkflowItem> getWorkflowItemsByWorkflowId(int workflowId){
		return workflowItemMapper.getWorkflowItemsByWorkflowId(workflowId);
	}

	/**
	 * Loeschen eines WorkflowItems aus der Datenbank
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @return der geaenderte Workflow
	 * @throws WorkflowItemNotFoundException
	 * @throws WorkflowNotEditableException
	 */
	@Transactional
	public Workflow deleteWorkflowItem(int workflowItemId) throws WorkflowItemNotFoundException, WorkflowNotEditableException {
		WorkflowItem workflowItem = workflowItemMapper.getWorkflowItemById(workflowItemId);
		if(workflowItem != null){
			Workflow workflow = workflowMapper.getWorkflowById(workflowItem.getWorkflowId());
			if(!workflow.isRunnable()){
				workflowItemMapper.deleteWorkflowItem(workflowItemId);
				return workflow;
			} else {
				throw new WorkflowNotEditableException();
			}
		} else {
			throw new WorkflowItemNotFoundException();
		}
	}

	/**
	 * Update der Position eines WorkflowItems
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @param xPos die uebergebene X-Position des WorkflowItems
	 * @param yPos die uebergebene Y-Position des WorkflowItems
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public void updatePosition(int workflowItemId, int xPos, int yPos){
		workflowItemMapper.updatePosition(workflowItemId, xPos, yPos);
	}

	/**
	 * Setzen des nachfolgenden WorkflowItems
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @param nextWorkflowItemIds die uebergebene ID das Nachfolger WorkflowItems
	 * @throws NextWorkflowItemException
	 * @throws WorkflowItemNotFoundException
	 * @throws WorkflowItemConnectionException
	 */
	@Transactional
	public void setNextWorkflowItems(int workflowItemId, List<Integer> nextWorkflowItemIds) throws NextWorkflowItemException, WorkflowItemNotFoundException, WorkflowItemConnectionException {
		WorkflowItem workflowItem = getWorkflowItemById(workflowItemId);
		List<Integer> previousWorkflowItems = workflowItemMapper.getPreviousWorkflowItems(workflowItemId);
		if (workflowItem == null) {
			throw new WorkflowItemNotFoundException();
		}

		if (nextWorkflowItemIds.contains(workflowItem.getId())) {
			throw new WorkflowItemConnectionException("A WorkflowItem can't be connected to itself!");
		}

		if(workflowItem.getType().equals("TASK") || workflowItem.getType().equals("SCRIPT") || workflowItem.getType().equals("JOIN")){
			for(Integer nextWorkflowItem : nextWorkflowItemIds){
				if(previousWorkflowItems.contains(nextWorkflowItem)){
					throw new WorkflowItemConnectionException("WorkflowItem can't be connected to its previous WorkflowItem");
				}
			}
		}

		List<Integer> currentNextWorkflowItemIds = workflowItemMapper.getNextWorkflowItems(workflowItemId);
		// Überprüfung, um was für ein WorkflowItem es sich handelt
		switch(workflowItem.getType()){
			case "START":
				if (nextWorkflowItemIds.size() > 1) {
					throw new NextWorkflowItemException("The start item can only have one next item!");
				} else if(currentNextWorkflowItemIds.size() == 1){
					workflowItemMapper.removeNextWorkflowItems(workflowItemId);
				}
				if(!nextWorkflowItemIds.isEmpty()) {
					workflowItemMapper.setNextWorkflowItems(workflowItemId, nextWorkflowItemIds);
				}
				break;
			case "END":
				throw new NextWorkflowItemException("The end item can't have next item!");
			case "TASK":
				if (nextWorkflowItemIds.size() > 1) {
					throw new NextWorkflowItemException("Task items can only have one next item!");
				} else if(currentNextWorkflowItemIds.size() == 1){
					workflowItemMapper.removeNextWorkflowItems(workflowItemId);
				}
				if(!nextWorkflowItemIds.isEmpty()) {
					workflowItemMapper.setNextWorkflowItems(workflowItemId, nextWorkflowItemIds);
				}
				break;
			case "DECISION":
				if (nextWorkflowItemIds.size() > 2) {
					throw new NextWorkflowItemException("Decision items can only have two next items!");
				} else if (!currentNextWorkflowItemIds.isEmpty()){
					workflowItemMapper.removeNextWorkflowItems(workflowItemId);
				}
				if(!nextWorkflowItemIds.isEmpty()) {
					workflowItemMapper.setNextWorkflowItems(workflowItemId, nextWorkflowItemIds);
				}
				break;
			case "SCRIPT":
				if (nextWorkflowItemIds.size() > 1) {
					throw new NextWorkflowItemException("Script items can only have one next item!");
				} else if(currentNextWorkflowItemIds.size() == 1){
					workflowItemMapper.removeNextWorkflowItems(workflowItemId);
				}
				if(!nextWorkflowItemIds.isEmpty()) {
					workflowItemMapper.setNextWorkflowItems(workflowItemId, nextWorkflowItemIds);
				}
				break;
			case "FORK":
				if (!currentNextWorkflowItemIds.isEmpty()){
					workflowItemMapper.removeNextWorkflowItems(workflowItemId);
				}
				if(!nextWorkflowItemIds.isEmpty())
					workflowItemMapper.setNextWorkflowItems(workflowItemId, nextWorkflowItemIds);
				break;
			case "JOIN":
				if (nextWorkflowItemIds.size() > 1) {
					throw new NextWorkflowItemException("Join items can only have one next item!");
				} else if(currentNextWorkflowItemIds.size() == 1){
					workflowItemMapper.removeNextWorkflowItems(workflowItemId);
				}
				if(!nextWorkflowItemIds.isEmpty()) {
					workflowItemMapper.setNextWorkflowItems(workflowItemId, nextWorkflowItemIds);
				}
				break;
			default:
				throw new WorkflowItemNotFoundException();
		}
	}

	/**
	 * Gibt ein WorkflowItem an Hand seiner ID zurueck
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @return das gesuchte WorkflowItem
	 */
	@Transactional
	public WorkflowItem getWorkflowItemById(int workflowItemId){
		return workflowItemMapper.getWorkflowItemById(workflowItemId);
	}

	/**
	 * Ueberpruefung ob der WorkflowItemType vorhanden ist
	 * @param type der uebergebene Typ
	 * @return true wenn es den WorkflowItemType gibt, sonst false
	 */
	@Transactional
	public boolean checkWorkflowItemType(String type) {
		return workflowItemMapper.checkWorkflowItemType(type);
	}

	/**
	 * Gibt einen Task an Hand seiner ID zurueck
	 * @param taskId die uebergebene ID das Task
	 * @return der gesuchte Task
	 */
	@Transactional
	public Task getTaskById(int taskId){
		return workflowItemMapper.getTaskById(taskId);
	}

	/**
	 * Gibt eine Decision an Hand ihrer ID zurueck
	 * @param workflowDecisionId die uebergebene ID das Decision
	 * @return die gesuchte Decision
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public WorkflowDecision getWorkflowDecisionById(int workflowDecisionId) throws WorkflowItemNotFoundException{
		WorkflowDecision workflowDecision = workflowItemMapper.getWorkflowDecisionById(workflowDecisionId);
		if(workflowDecision == null){
			throw new WorkflowItemNotFoundException();
		}
		return workflowDecision;
	}

	/**
	 * Gibt ein Script an Hand seiner ID zurueck
	 * @param workflowScriptId die uebergebene ID des Scripts
	 * @return das gesuchte Script
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public WorkflowScript getWorkflowScriptById(int workflowScriptId) throws WorkflowItemNotFoundException {
		WorkflowScript workflowScript = workflowItemMapper.getWorkflowScriptById(workflowScriptId);
		if(workflowScript == null)
			throw new WorkflowItemNotFoundException();
		
		return workflowScript;
	}

	/**
	 * Locken eines WorkflowItems in der Datenbank
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @throws WorkflowItemLockException
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public void lockWorkflowItem(int workflowItemId) throws WorkflowItemLockException, WorkflowItemNotFoundException {
		if(workflowItemMapper.getWorkflowItemById(workflowItemId) == null){
			throw new WorkflowItemNotFoundException();
		}
		if(!workflowItemMapper.isWorkflowItemLocked(workflowItemId)){
			workflowItemMapper.lockWorkflowItem(workflowItemId);
		} else {
			throw new WorkflowItemLockException();
		}
	}

	/**
	 * Unlocken eines WorkflowItems in der Datenbank
	 * @param workflowItemId die uebergebene ID des Workflowitems
	 * @throws WorkflowItemNotFoundException
	 * @throws WorkflowItemLockException
	 */
	@Transactional
	public void unlockWorkflowItem(int workflowItemId) throws WorkflowItemNotFoundException, WorkflowItemLockException {
		if(workflowItemMapper.getWorkflowItemById(workflowItemId) == null){
			throw new WorkflowItemNotFoundException();
		}
		if(workflowItemMapper.isWorkflowItemLocked(workflowItemId)){
			workflowItemMapper.unlockWorkflowItem(workflowItemId);
		} else {
			throw new WorkflowItemLockException();
		}
	}

	/**
	 * Ueberpruefung ob ein WorkflowItem gelocked ist
	 * @param workflowId die uebergebene ID des WorkflowItems
	 * @return true wenn das WorkflowItem gelocked ist, sonst false
	 */
	@Transactional
	public boolean isWorkflowItemLocked(int workflowId){
		return workflowItemMapper.isWorkflowItemLocked(workflowId);
	}

	/**
	 * Zuweisung von UserGroups zu einem WorkflowItem
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @param groupIds die uebergebene Liste von UserGroups
	 */
	@Transactional
	public void setUserGroupsForWorkflowItem(int workflowItemId, List<Integer> groupIds){
		workflowItemMapper.removeAllUserGroupsFromWorkflowItem(workflowItemId);
		workflowItemMapper.setUserGroupsForTask(workflowItemId, groupIds);
	}

	/**
	 * Entfernen der Zuweisung einer UserGroup zu einerm WorkflowItem
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @param userGroupId die uebergebene ID der UserGroup
	 */
	@Transactional
	public void removeUserGroupFromWorkflowItem(int workflowItemId, int userGroupId){
		workflowItemMapper.removeUserGroupFromWorkflowItem(workflowItemId, userGroupId);
	}

	/**
	 * Gibt eine Liste von UserGroups zurueck die einem WorkflowItem zugeordnet sind
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @return die Liste von UserGroups eines WorkflowItems
	 */
	@Transactional
	public List<Integer> getUserGroupForWorkflowItem(int workflowItemId){
		return workflowItemMapper.getUserGroupForWorkflowItem(workflowItemId);
	}

	/**
	 * Gibt eine Liste von FromGroups fuer einen Task zurueck
	 * @param workflowItemId die uebergebene ID des Task (WorkflowItem)
	 * @return die Liste der FormGroups eines Task
	 */
	@Transactional
	public List<FormGroupForTask> getComponentsAndFormGroupsForTask(int workflowItemId){
		return workflowItemMapper.getComponentsAndFormGroupsForTask(workflowItemId);
	}

	/**
	 * Erstellen einer TaskComponent in der Datenbank
	 * @param taskComponent die uebergebene TaskComponent
	 */
	@Transactional
	public void createTaskComponent(TaskComponent taskComponent){
		workflowItemMapper.createTaskComponent(taskComponent);
	}

	/**
	 * Loeschen einer TaskComponent aus der Datenbank
	 * @param taskComponentId die uebergebene ID der TaskComponent
	 */
	@Transactional
	public void deleteTaskComponent(int taskComponentId){
		workflowItemMapper.deleteTaskComponent(taskComponentId);
	}

	/**
	 * Entfernen einer TaskComponent aus einem WorkflowItem
	 * @param workflowItemId die uebergebene ID des WorkflowItems
	 * @param taskComponentId die uebergebene ID der TaskComponent
	 */
	@Transactional
	public void removeComponentFromWorkflowItem(int workflowItemId, int taskComponentId){
		workflowItemMapper.removeComponentFromWorkflowItem(workflowItemId, taskComponentId);
	}

	/**
	 * Gibt den Typ einer TaskComponent zurueck
	 * @param taskComponentId die uebergebene ID der TaskComponent
	 * @return der Typ der TaskComponent
	 */
	@Transactional
	public String getTaskComponentType(int taskComponentId){
		return workflowItemMapper.getTaskComponentType(taskComponentId);
	}

	/**
	 * Update eines Tasks in der Datenbank
	 * @param taskId die uebergebene ID des Task
	 * @param name der uebergebene Name des Task
	 * @param usergroupIds die uebergebene Liste von UserGroups des Task
	 * @param taskComponentsForTask die uebergebene Liste von TaskComponents des Task
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public void updateTask(int taskId, String name, List<Integer> usergroupIds, List<TaskComponentForTask> taskComponentsForTask) throws WorkflowItemNotFoundException {
		Task task = workflowItemMapper.getTaskById(taskId);
		if(task != null){
			task.setName(name);
			workflowItemMapper.removeAllComponentsFromWorkflowItem(taskId);
			workflowItemMapper.removeAllUserGroupsFromWorkflowItem(taskId);
			workflowItemMapper.updateWorkflowItem(task);
			workflowItemMapper.setUserGroupsForTask(taskId, usergroupIds);
			workflowItemMapper.setComponentsForTask(taskId, taskComponentsForTask);
		}else{
			throw new WorkflowItemNotFoundException();
		}
	}

	/**
	 * Update einer Decision in der Datenbank
	 * @param workflowDecisionId die uebergebene ID der Decision
	 * @param variables die uebergebene Liste der Variablen der Decision
	 * @param condition die uebergebene Condition der Decision
	 * @param nextWorkflowItemOnTrue die uebergebene ID des Nachfolger WorkflowItems, dass bei Decision == true folgt
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public void updateWorkflowDecision(int workflowDecisionId, List<TaskVariable> variables, String condition, int nextWorkflowItemOnTrue) throws WorkflowItemNotFoundException{
		WorkflowDecision workflowDecision = workflowItemMapper.getWorkflowDecisionById(workflowDecisionId);
		if(workflowDecision != null){
			workflowItemMapper.removeConditionAndNextOnTrueFromWorkflowDecision(workflowDecisionId);
			workflowItemMapper.removeVariablesFromWorkflowItem(workflowDecisionId);
			workflowItemMapper.setConditionAndNextOnTrueForWorkflowDecision(workflowDecisionId, condition, nextWorkflowItemOnTrue);
			
			if(!variables.isEmpty())
				workflowItemMapper.setVariablesForWorkflowItem(workflowDecisionId, variables);
		}else{
			throw new WorkflowItemNotFoundException();
		}
	}

	/**
	 * Update eines Script in der Datenbank
	 * @param workflowScriptId die uebergebene ID das Scripts
	 * @param variables die uebergebene Liste von Variablen des Scripts
	 * @param script das uebergebene Script
	 * @throws WorkflowItemNotFoundException
	 */
	@Transactional
	public void updateWorkflowScript(int workflowScriptId, List<TaskVariable> variables, String script) throws WorkflowItemNotFoundException {
		WorkflowScript workflowScript = workflowItemMapper.getWorkflowScriptById(workflowScriptId);
		if(workflowScript != null){
			workflowItemMapper.removeScriptFromWorkflowScript(workflowScriptId);
			workflowItemMapper.removeVariablesFromWorkflowItem(workflowScriptId);
			workflowItemMapper.setScriptForWorkflowScript(workflowScriptId, script);
			
			if(!variables.isEmpty())
				workflowItemMapper.setVariablesForWorkflowItem(workflowScriptId, variables);
		}else{
			throw new WorkflowItemNotFoundException();
		}
	}

	/**
	 * Gibt alle Tasks eines Workflows zurueck
	 * @param workflowId die uebergebene ID des Workflows
	 * @return die Liste der Tasks eines Workflows
	 */
	@Transactional
	public List<Task> getAllTasks (int workflowId){
		return workflowItemMapper.getAllTasks(workflowId);
	}
}
