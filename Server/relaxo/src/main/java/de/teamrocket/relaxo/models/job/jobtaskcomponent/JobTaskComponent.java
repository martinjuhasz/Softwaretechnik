package de.teamrocket.relaxo.models.job.jobtaskcomponent;

/**
 * Diese Klasse hält die einzelnen Komponenten des jeweiligen JobTasks.
 */
public abstract class JobTaskComponent {

    /**
     * Die id des JobTaskComponent.
     */
    private int id;

    /**
     * Die TaskComponente, auf der sich diese JobTaskComponent bezieht.
     */
    private int taskComponentId;

    /**
     * Der JobTask, auf der sich diese TaskComponent bezieht.
     */
    private int jobTaskId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskComponentId() {
        return taskComponentId;
    }

    public void setTaskComponentId(int taskComponentId) {
        this.taskComponentId = taskComponentId;
    }

    public int getJobTaskId() {
        return jobTaskId;
    }

    public void setJobTaskId(int jobTaskId) {
        this.jobTaskId = jobTaskId;
    }

    public abstract void accept(JobTaskComponentVisitor jobTaskComponentVisitor);
}