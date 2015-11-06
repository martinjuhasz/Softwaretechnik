package de.teamrocket.relaxo.rest.ressources;

import de.teamrocket.relaxo.controller.exceptions.UserNotFoundException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.job.JobTask;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobUpdateComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.job.request.JobUpdateComponentRequest;
import de.teamrocket.relaxo.rest.models.job.request.JobUpdateRequest;
import de.teamrocket.relaxo.rest.models.job.response.JobResponse;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * REST-Ressource um Jobs zu verwalten.
 */
@Path("jobs")
@CheckToken
public class JobRessource extends RestRessource {

    // Static

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

    // Methods

    /**
     * Gibt Details eines Jobs zurück.
     *
     * @param jobId        ID des Jobs
     * @param token        Session token um sich zu authentifizieren
     * @param filterByTask Filterung der Jobtasks des Jobs anhand der TaskID, optional
     * @return Job Details als JSON
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJob(@PathParam("id") int jobId, @HeaderParam("Token") String token, @QueryParam("filter_by_task") Integer filterByTask) {

        LOGGER.info("GET-Request auf /jobs/{id}");

        // return error if job wasn't found
        Job job = jobController.getJob(jobId);
        if (job == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Job ID not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        List<JobTask> jobTasks;

        if (filterByTask != null) {
            jobTasks = jobController.getJobTasks(jobId, filterByTask);
        } else {
            jobTasks = jobController.getJobTasks(jobId);
        }


        User creator = null;
        try {
            creator = userManagementController.getUserById(job.getCreatorId());
        } catch (UserNotFoundException e) {
            LOGGER.warning(e.getMessage(), e);
        }
        String userName;
        if(creator!=null){
            userName = creator.getName();
        }else{
            userName = "creator is null";
        }

        RestEnvelope<JobResponse> envelope = new RestEnvelope<>();
        JobResponse jobResponse = new JobResponse(job, jobTasks, userName);
        envelope.setData(jobResponse);
        return Response.ok(envelope).build();

    }

    /**
     * Aktualisiert einen Job anhand des Payloads.
     *
     * @param jobUpdateRequest Payload der Jobänderungen
     * @param jobId            ID des Jobs
     * @param token            Session token um sich zu authentifizieren
     * @return Job Details als JSON
     */
    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJob(JobUpdateRequest jobUpdateRequest, @PathParam("id") int jobId, @HeaderParam("Token") String token) {

        LOGGER.info("POST-Request auf /jobs/{id}");

        Job job = jobController.getJob(jobId);

        if (job == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Job " + jobId + " not found.");
            envelope.setError(error);

            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        User user = userManagementController.getUserByToken(token);

        List<JobUpdateComponent> components = new LinkedList<>();
        JobUpdateComponent jobUpdateComponent;
        for (JobUpdateComponentRequest jobUpdateComponentRequest : jobUpdateRequest.getComponents()) {
            jobUpdateComponent = new JobUpdateComponent(jobUpdateComponentRequest.getTaskComponentId(), jobUpdateComponentRequest.getValue());
            components.add(jobUpdateComponent);
        }


        if(taskController.getTask(jobUpdateRequest.getTaskId())==null){
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Task " + jobUpdateRequest.getTaskId() + " not found.");
            envelope.setError(error);

            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }




        jobController.updateJobTask(jobId, jobUpdateRequest.getTaskId(), components, user.getId());

        return Response.ok().build();
    }
}
