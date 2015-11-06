package de.teamrocket.relaxo.rest.models.task.request;

import java.util.List;

public class TaskUpdateRequest {
    private String name;
    private List<Integer> usergroups;
    private List<TaskComponentForTaskRequest> taskComponentsForTask;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUsergroups() {
        return usergroups;
    }

    public void setUsergroups(List<Integer> usergroups) {
        this.usergroups = usergroups;
    }

    public List<TaskComponentForTaskRequest> getTaskComponentsForTask() {
        return taskComponentsForTask;
    }

    public void setTaskComponentsForTask(
            List<TaskComponentForTaskRequest> taskComponentsForTask) {
        this.taskComponentsForTask = taskComponentsForTask;
    }
}
