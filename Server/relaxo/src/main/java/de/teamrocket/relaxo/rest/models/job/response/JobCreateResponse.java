package de.teamrocket.relaxo.rest.models.job.response;


/**
 * Created by jthei001 on 03.12.2014.
 */
public class JobCreateResponse {

    //

    private Integer jobId;
    private Integer taskId;

    //

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
