package de.teamrocket.relaxo.models.job.jobtaskcomponent;

import java.math.BigDecimal;

public class JobTaskComponentFloat extends JobTaskComponent {
    /**
     * Der Wert dieser JobTaskComponente.
     */
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public void accept(JobTaskComponentVisitor jobTaskComponentVisitor) {
        jobTaskComponentVisitor.visit(this);
    }
}
