package de.teamrocket.relaxo.rest.models.task.response;

import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.rest.models.formgroup.response.FormGroupResponse;
import de.teamrocket.relaxo.rest.models.formgroup.response.FormGroupResponseFactory;
import de.teamrocket.relaxo.rest.models.workflow.response.TaskResponse;

import java.util.LinkedList;
import java.util.List;

/**
 * Antwort des REST-Servers auf Anfragen nach Taskdetails
 */
public class TaskDetailResponse extends TaskResponse {

    /**
     * Liste der FormGroups eines Tasks
     */
    private List<FormGroupResponse> formGroups;

    /**
     * Liste der UserGroups eines Tasks
     */
    private List<Integer> userGroups;

    /**
     * Leerer Konstruktor
     */
    public TaskDetailResponse() {
        this.formGroups = new LinkedList<>();
        this.userGroups = new LinkedList<>();
    }

    /**
     * Erstellt einen TaskDetailResponse anhand des übergebenen Tasks
     *
     * @param task das zugehörige Taskobjekt
     */
    public TaskDetailResponse(Task task, List<Integer> userGroups, List<FormGroup> formGroups, FormGroupResponseFactory formGroupResponseFactory) {
        super(task);
        this.userGroups = userGroups;

        this.formGroups = new LinkedList<>();

        FormGroupResponse formGroupResponse;
        for (FormGroup formGroup : formGroups) {
            formGroupResponse = formGroupResponseFactory.create(formGroup);
            this.formGroups.add(formGroupResponse);
        }
    }

    public List<FormGroupResponse> getFormGroups() {
        return formGroups;
    }

    public void setFormGroups(List<FormGroupResponse> formGroups) {
        this.formGroups = formGroups;
    }

    public List<Integer> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<Integer> userGroups) {
        this.userGroups = userGroups;
    }
}
