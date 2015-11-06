package de.teamrocket.relaxo.rest.models.job.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponent;

import java.util.Date;
import java.util.List;


/**
 * REST-Server Antwort auf einen JobTask
 */
public class JobTaskResponse {

    //

    /**
     * Startzeit
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
    private Date startTime;

    /**
     * Endzeit
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "CET")
    private Date endTime;

    /**
     * JobTaskComponents des Jobs
     */
    private List<JobTaskComponentResponse> jobTaskComponentRespones;

    /**
     * ID
     */
    private int id;


    /**
     * Kontruktor zum Erstellen einer JobTaskResponse
     *
     * @param jobTask der zugehörige JobTask
     */
    public JobTaskResponse(JobTask jobTask) {
        this.startTime = jobTask.getStartTime();
        this.endTime = jobTask.getEndTime();
        this.id = jobTask.getId();

        JobTaskComponentResponseVisitor visitor = new JobTaskComponentResponseVisitor();

        for (JobTaskComponent jobTaskComponent : jobTask.getJobTaskComponents()) {

            jobTaskComponent.accept(visitor);
        }
        this.jobTaskComponentRespones = visitor.getJobTaskComponentResponses();
    }

    //

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

    public List<JobTaskComponentResponse> getJobTaskComponentResponses() {
        return jobTaskComponentRespones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJobTaskComponents(List<JobTaskComponentResponse> jobTaskComponents) {
        this.jobTaskComponentRespones = jobTaskComponents;
    }
}
