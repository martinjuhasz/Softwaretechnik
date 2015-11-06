package de.teamrocket.relaxo.controller;

import de.teamrocket.relaxo.controller.exceptions.FieldMissingException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.TaskComponentForTask;
import de.teamrocket.relaxo.models.workflow.Workflow;

import java.util.List;


/**
 * Der TaskController stellt Methoden bereit, um spezifisch auf Tasks zuzugreifen.
 */
public interface TaskController {

    /**
     * Gibt alle Task wieder, die der user in einem entsprechenden workflow hat.
     *
     * @param workflow Das workflow, auf das geprüft werden soll.
     * @param user     Der User.
     * @return Eine Liste, mit allen Tasks, auch die, die nicht active/running sind.
     */
    public List<Task> getTasks(Workflow workflow, User user);

    /**
     * Liefert den Task mit der uebergebenen Id
     *
     * @param taskId Die Id des Tasks
     * @return Den Task
     */
    public Task getTask(int taskId);

    /**
     * Gibt zurück, ob der User den übergebenen Task sehen darf.
     *
     * @param user Der zu prüfende User.
     * @param task Der zu prüfende Task.
     * @return Gibt wahr zurück, falls der User diesen Task sehen darf.
     */
    public boolean isUserAbleToSeeTask(User user, Task task);

    /**
     * Gibt die IDs der Usergruppen eines Tasks zurück
     *
     * @param task der gewünschte Task
     * @return Liste der IDs der Usergruppen
     */
    public List<Integer> getUserGroupsForTask(Task task);

    /**
     * Fügt Usergruppen einer Task hinzu
     *
     * @param task     die Task
     * @param groupIds Liste der IDs aller Usergruppen die hinzugefügt werden solen
     */
    public void setUserGroupsForTask(Task task, List<Integer> groupIds);

    /**
     * Liefert die Formgroups eines Tasks inklusive zugehöriger Components
     *
     * @param task der Task
     * @return Liste der FormGroups
     */
    public List<FormGroup> getFormGroupsForTask(Task task);

	/**
	 * Aktuallisiert die Daten eines Tasks mit der übergebenen ID.
     *
	 * @param taskId die ID des Tasks
	 * @param name der Task-Name
	 * @param usergroups die UserGroups, die dieses bearbeiten können
	 * @param taskComponentsForTask eine Liste mit die TaskComponents
	 * @throws WorkflowItemNotFoundException
	 */
	public void updateTask(int taskId, String name, List<Integer> usergroups, List<TaskComponentForTask> taskComponentsForTask) throws WorkflowItemNotFoundException, FieldMissingException;
}
