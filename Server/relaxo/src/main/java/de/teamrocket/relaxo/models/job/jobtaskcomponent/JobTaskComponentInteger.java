package de.teamrocket.relaxo.models.job.jobtaskcomponent;

/**
 * Beinhaltet Daten des JobTasks.
 */
public class JobTaskComponentInteger extends JobTaskComponent {

    /**
     * Der Wert dieser JobTaskComponente.
     */
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void accept(JobTaskComponentVisitor jobTaskComponentVisitor) {
        jobTaskComponentVisitor.visit(this);
    }

}