package de.teamrocket.relaxo.models.job;


/**
 * Diese Klasse ist eine Repraesentation eines Job-WorkflowItems. Sie verweist auf ein WorkflowItem des Workflows, aus
 * dem dieses entspringt.
 */
public class JobWorkflowItem {

    /**
     * Die Id des JobWorkflowItem
     */
    private int id;

    /**
     * Das Workflowitem, auf das dieses JobWorkflowItem verweist.
     */
    private int workflowItemId;

    /**
     * Der Job, auf das sich dieses JobWorkflowItem bezieht.
     */
    private int jobId;

    /**
     * Gibt an, ob dieses WorkflowItem vollständig abgearbeitet ist.
     */
    private boolean done;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkflowItemId() {
        return workflowItemId;
    }

    public void setWorkflowItemId(int workflowItemId) {
        this.workflowItemId = workflowItemId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
