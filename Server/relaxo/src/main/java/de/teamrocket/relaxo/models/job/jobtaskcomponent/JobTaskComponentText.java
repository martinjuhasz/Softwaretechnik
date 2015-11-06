package de.teamrocket.relaxo.models.job.jobtaskcomponent;


/**
 * Beinhaltet Daten des JobTasks.
 */
public class JobTaskComponentText extends JobTaskComponent {

    /**
     * Der Wert dieser JobTaskComponente.
     */
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(JobTaskComponentVisitor jobTaskComponentVisitor) {
        jobTaskComponentVisitor.visit(this);
    }
}