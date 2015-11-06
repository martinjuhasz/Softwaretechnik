package de.teamrocket.relaxo.models.workflow;

/**
 * Eine Bean zum Speichern des Zusammenhangs zwischen einem TaskComponent und der readOnly Eigenschaft in einem Task.
 * Wird verwendet, um bei einer Task Definition die TaskComponents und die readOnly Eigenschaft einem Task zuzuordnen.
 */
public class TaskComponentForTask {
	
	/**
	 * Die Id des TaskComponents
	 */
    private int id;
    /**
     * Die readOnly Eigenschaft (Bearbeitbarkeit des TaskComponents an einem Task)
     */
    private boolean readOnly;

    public TaskComponentForTask() {

    }

    /**
     * Erzeugt eine neues TaskComponentForTask mit der übergebenen TaskComponentId und readOnly Eigenschaft
     * @param id Die Id des TaskComponents
     * @param readOnly Die readOnly Eigenschaft
     */
    public TaskComponentForTask(int id, boolean readOnly) {
        this.id = id;
        this.readOnly = readOnly;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
