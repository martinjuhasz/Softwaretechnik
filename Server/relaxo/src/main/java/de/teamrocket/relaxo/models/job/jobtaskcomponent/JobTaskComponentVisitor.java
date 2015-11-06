package de.teamrocket.relaxo.models.job.jobtaskcomponent;

/**
 * Ein Interface, um ein Double Dispatch für ein JobTaskComponent auszuführen
 */
public interface JobTaskComponentVisitor {
    public void visit(JobTaskComponentInteger jobTaskComponentInteger);

    public void visit(JobTaskComponentText jobTaskComponentText);

    public void visit(JobTaskComponentDate jobTaskComponentDate);

    public void visit(JobTaskComponentFloat jobTaskComponentFloat);
}
