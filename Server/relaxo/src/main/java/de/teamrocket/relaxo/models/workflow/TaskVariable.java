package de.teamrocket.relaxo.models.workflow;

/**
 * Eine Bean zum Speichern eines Variablennames und die zugehörigen Ids für Task und TaskComponent.
 * WorkflowDecision und WorkflowScript können so eindeutig auf ein TaskComponents eines Tasks zugreifen.
 */
public class TaskVariable {
	/**
	 * Der Name der Variable
	 */
    private String name;
    /**
     * Die Id des Tasks
     */
    private int taskId;
    /**
     * Die Id des TaskComponent
     */
    private int taskComponentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskComponentId() {
        return taskComponentId;
    }

    public void setTaskComponentId(int taskComponentId) {
        this.taskComponentId = taskComponentId;
    }
}
