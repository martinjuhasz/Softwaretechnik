package de.teamrocket.relaxo.rest.models.job.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobTask;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * REST-Server Antwort auf eine Jobanfrage
 */
public class JobResponse {

    //

    /**
     * id des jobs
     */
    private int id;

    /**
     * Ersteller des Jobs
     */
    @JsonInclude(Include.NON_NULL)
    private String creator;

    /**
     * Startzeit des Jobs
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
    private Date startTime;

    /**
     * Endzeit des Jobs
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
    private Date endTime;

    /**
     * JobTasks des Jobs
     */
    @JsonInclude(Include.NON_NULL)
    private List<JobTaskResponse> jobTasks;

    /**
     * Ist der Job momentan gesperrt
     */
    @JsonInclude(Include.NON_NULL)
    private Boolean locked = true;


    public JobResponse() {

    }

    public JobResponse(Job job) {
        this(job, null, null);
    }

    public JobResponse(Job job, String creator) {
        this(job, null, creator);
    }

    public JobResponse(Job job, Boolean locked){
        this(job, null, null, locked);
    }

    public JobResponse(Job job, List<JobTask> jobTasks, String creator) {
        this(job, jobTasks, creator, null);
    }

    /**
     * Konstruktor zum befüllen des Responses
     *
     * @param job der Job aus dem der JobResponse erstellt werden soll
     */
    public JobResponse(Job job, List<JobTask> jobTasks, String creator, Boolean locked) {
        this.id = job.getId();
        this.startTime = job.getStartTime();
        this.endTime = job.getEndTime();
        this.creator = creator;
        this.locked = locked;

        if (jobTasks != null) {
            this.jobTasks = new LinkedList<>();
            for (JobTask jobTask : jobTasks) {
                JobTaskResponse jobTaskResponse = new JobTaskResponse(jobTask);
                this.jobTasks.add(jobTaskResponse);
            }
        }
    }

    //

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<JobTaskResponse> getJobTasks() {
        return jobTasks;
    }

    public void setJobTasks(List<JobTaskResponse> jobTasks) {
        this.jobTasks = jobTasks;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }
}
