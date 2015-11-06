package de.teamrocket.relaxo.models.job;

import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponent;

import java.util.Date;
import java.util.List;

/**
 * Diese Klasse ist eine genauere Ausprägung eines JobWorkflowItems.
 */
public class JobTask extends JobWorkflowItem {

    /**
     * Gibt an, wann dieser Task erstellt wurde.
     */
    private Date startTime;

    /**
     * Gibt an, wann dieser Task beendet wurde.
     */
    private Date endTime;

    /**
     * Gibt an, welcher User diesen Task gelockt hat. Sofern ein Task gelockt ist, kann dieser von keinem anderen
     * User nocheinmal gelockt werden, bis unlock aufgerufen wurde.
     */
    private Integer blockerUserId;

    /**
     * Die id des Bearbeiters.
     */
    private int editorUserId;

    /**
     * Die JobTaskComponents
     */
    private List<JobTaskComponent> jobTaskComponents;

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

    public Integer getBlockerUserId() {
        return blockerUserId;
    }

    public void setBlockerUserId(Integer blockerUserId) {
        this.blockerUserId = blockerUserId;
    }

    public int getEditorUserId() {
        return editorUserId;
    }

    public void setEditorUserId(int editorUserId) {
        this.editorUserId = editorUserId;
    }

    public List<JobTaskComponent> getJobTaskComponents() {
        return jobTaskComponents;
    }

    public void setJobTaskComponents(List<JobTaskComponent> jobTaskComponents) {
        this.jobTaskComponents = jobTaskComponents;
    }

    /**
     * Gibt Jobtask [<id>] zurück.
     *
     * @return Der Returnstring.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("JobTask ");
        sb.append("[").append(this.getId()).append("]");
        return sb.toString();
    }
}