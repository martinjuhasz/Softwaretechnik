package de.teamrocket.relaxo.rest.models.job.response;

import de.teamrocket.relaxo.models.job.jobtaskcomponent.*;
import de.teamrocket.relaxo.models.taskcomponent.ComponentType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class JobTaskComponentResponseVisitor implements JobTaskComponentVisitor {

    private List<JobTaskComponentResponse> jobTaskComponentResponses;

    public JobTaskComponentResponseVisitor() {
        this.jobTaskComponentResponses = new LinkedList<>();
    }

    public List<JobTaskComponentResponse> getJobTaskComponentResponses() {
        return jobTaskComponentResponses;
    }

    public void setJobTaskComponentResponses(List<JobTaskComponentResponse> jobTaskComponentResponses) {
        this.jobTaskComponentResponses = jobTaskComponentResponses;
    }

    @Override
    public void visit(JobTaskComponentInteger jobTaskComponentInteger) {
        String type = ComponentType.TYPE_INTEGER.toString();
        int taskComponentId = jobTaskComponentInteger.getTaskComponentId();
        int value = jobTaskComponentInteger.getValue();
        JobTaskComponentResponse jobTaskComponentResponse = new JobTaskComponentObjectResponse(type, taskComponentId, value);
        jobTaskComponentResponses.add(jobTaskComponentResponse);
    }

    @Override
    public void visit(JobTaskComponentText jobTaskComponentText) {
        String type = ComponentType.TYPE_TEXT.toString();
        int taskComponentId = jobTaskComponentText.getTaskComponentId();
        String value = jobTaskComponentText.getValue();
        JobTaskComponentResponse jobTaskComponentResponse = new JobTaskComponentObjectResponse(type, taskComponentId, value);
        jobTaskComponentResponses.add(jobTaskComponentResponse);
    }

    @Override
    public void visit(JobTaskComponentDate jobTaskComponentDate) {
        String type = ComponentType.TYPE_DATE.toString();
        int taskComponentId = jobTaskComponentDate.getTaskComponentId();
        Date value = jobTaskComponentDate.getValue();
        JobTaskComponentResponse jobTaskComponentResponse = new JobTaskComponentDateResponse(type, taskComponentId, value);
        jobTaskComponentResponses.add(jobTaskComponentResponse);
    }

    @Override
    public void visit(JobTaskComponentFloat jobTaskComponentFloat) {
        String type = ComponentType.TYPE_FLOAT.toString();
        int taskComponentId = jobTaskComponentFloat.getTaskComponentId();
        BigDecimal value = jobTaskComponentFloat.getValue();
        JobTaskComponentResponse jobTaskComponentResponse = new JobTaskComponentObjectResponse(type, taskComponentId, value);
        jobTaskComponentResponses.add(jobTaskComponentResponse);
    }
}
