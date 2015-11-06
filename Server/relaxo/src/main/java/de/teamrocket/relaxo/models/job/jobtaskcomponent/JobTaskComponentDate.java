package de.teamrocket.relaxo.models.job.jobtaskcomponent;

import java.util.Date;

/**
 * Beinhaltet Daten des JobTasks.
 */
public class JobTaskComponentDate extends JobTaskComponent {

    /**
     * Der Wert dieser JobTaskComponente.
     */
    private Date value;

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public void accept(JobTaskComponentVisitor jobTaskComponentVisitor) {
        jobTaskComponentVisitor.visit(this);
    }
}