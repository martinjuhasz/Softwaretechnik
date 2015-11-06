package de.teamrocket.relaxo.models.job;

import java.util.Date;

/**
 * Alle Jobs des Systems werden durch diese Klasse dargestellt. Dieser durchlaeuft einen definierten Workflow, auf den dieser auch verweist.
 * An diesen Job werden die einzelnen Komponenten (Daten) angeheftet.
 */
public class Job {

    /**
     * Die id des Jobs.
     */
    private int id;

    /**
     * Der Workflow, auf den sich dieser Job bezieht.
     */
    private int workflowId;

    /**
     * Der User, der diesen Job erstellt hat.
     */
    private int creatorId;

    /**
     * Die Zeit, an dem dieser Job erstellt wurde.
     */
    private Date startTime;

    /**
     * Die End-Zeit, wann dieser Job vollständig abgeschloßen wurde. Das heißt, sobald keine laufenden oder
     * wartenden Tasks mehr für diesen Job existieren.
     */
    private Date endTime;

    /**
     * Gibt an, ob der Job active ist, also ob dieser bearbeitbar ist. Dieser kann z.B. temporaer auf
     * false gesetzt werden.
     */
    private boolean active = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}