package de.teamrocket.relaxo.rest.ressources;

import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.exceptions.FieldMissingException;
import de.teamrocket.relaxo.controller.exceptions.JobTaskLockException;
import de.teamrocket.relaxo.controller.exceptions.JobTaskNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.TaskComponentForTask;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.job.response.JobResponse;
import de.teamrocket.relaxo.rest.models.task.request.TaskComponentForTaskRequest;
import de.teamrocket.relaxo.rest.models.task.request.TaskUpdateRequest;
import de.teamrocket.relaxo.rest.models.task.response.TaskDetailResponse;
import de.teamrocket.relaxo.rest.models.task.response.TaskDetailResponseFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * REST-Ressource um Tasks zu verwalten.
 */
@Path("tasks")
@CheckToken
public class TaskRessource extends RestRessource {

    // Vars

    /**
     * Instanz der TaskDetailResponseFactory
     */
    private final TaskDetailResponseFactory taskDetailResponseFactory;

    // Methods

    @Inject
    public TaskRessource(TaskDetailResponseFactory taskDetailResponseFactory) {
        this.taskDetailResponseFactory = taskDetailResponseFactory;
    }

    /**
     * Gibt Details eines Tasks zurück.
     *
     * @param taskId ID des Tasks
     * @param token  session token um sich zu authentifizieren
     * @return Task Details als JSON
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getTaskById(@PathParam("id") int taskId, @HeaderParam("Token") String token) {

        LOGGER.info("GET-Request auf /tasks/{id}");

        Task task = taskController.getTask(taskId);
        // error if task wasn't found
        if (task == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Task ID not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        User user = userManagementController.getUserByToken(token);
        boolean hasRights = taskController.isUserAbleToSeeTask(user, task);
        // error if user has no rights to view task
        if (!hasRights) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("missing_rights", "You have no rights to view this Task");
            envelope.setError(error);
            return Response.status(CODE_401_UNAUTHORIZED).entity(envelope).build();
        }

        List<Integer> userGroups = taskController.getUserGroupsForTask(task);
        List<FormGroup> formGroups = taskController.getFormGroupsForTask(task);

        RestEnvelope<TaskDetailResponse> envelope = new RestEnvelope<>();
        TaskDetailResponse sessionResponse = taskDetailResponseFactory.create(task, userGroups, formGroups);
        envelope.setData(sessionResponse);
        return Response.ok(envelope).build();
    }


    /**
     * Liste aller Jobs eines Tasks.
     *
     * @param taskId ID des Tasks
     * @param token  session token um sich zu authentifizieren
     * @return Job Kollektion als JSON
     */
    @GET
    @Path("{id}/jobs")
    @Produces("application/json")
    public Response getJobsForTask(@PathParam("id") int taskId, @HeaderParam("Token") String token) {

        LOGGER.info("GET-Request auf /tasks/{id}/jobs");

        Task task = taskController.getTask(taskId);
        // error if task wasn't found
        if (task == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Task ID not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        User user = userManagementController.getUserByToken(token);
        boolean hasRights = taskController.isUserAbleToSeeTask(user, task);
        // error if user has no rights to view task
        if (!hasRights) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("missing_rights", "You have no rights to view this Task");
            envelope.setError(error);
            return Response.status(CODE_401_UNAUTHORIZED).entity(envelope).build();
        }


        Map<Job, Boolean> jobs = jobController.getJobsForTask(task);


        List<JobResponse> jobResponses = new LinkedList<>();
        for (Map.Entry<Job, Boolean> entry : jobs.entrySet()) {
            jobResponses.add(new JobResponse(entry.getKey(), entry.getValue()));
        }

        RestEnvelope<List<JobResponse>> envelope = new RestEnvelope<>();
        envelope.setData(jobResponses);
        return Response.ok(envelope).build();
    }

    /**
     * Update eines Task.
     *
     * @param taskId ID des Tasks
     * @return
     */
    @POST
    @Path("{id}")
    @Produces("application/json")
    public Response updateTask(@PathParam("id") int taskId, TaskUpdateRequest taskUpdateRequest) {

        LOGGER.info("POST-Request auf /tasks/{id}");

        String name = taskUpdateRequest.getName();
        List<Integer> usergroups = taskUpdateRequest.getUsergroups();

        List<TaskComponentForTask> taskComponentsForTask = new LinkedList<>();
        for (TaskComponentForTaskRequest taskComponentForTaskRequest : taskUpdateRequest.getTaskComponentsForTask()) {
            taskComponentsForTask.add(new TaskComponentForTask(taskComponentForTaskRequest.getId(), taskComponentForTaskRequest.isReadOnly()));
        }
        try {
            taskController.updateTask(taskId, name, usergroups, taskComponentsForTask);
        } catch (WorkflowItemNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        } catch (FieldMissingException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        }
        return Response.ok().build();
    }

    /**
     * Locken eines Jobs eines Task.
     *
     * @param taskId ID des Tasks
     * @param jobId ID des Jobs
     * @param token  session token um sich zu authentifizieren
     * @return
     */
    @POST
    @Path("{taskId}/jobs/{jobId}/lock")
    @Produces("application/json")
    public Response lockJobAtTaskPosition(@PathParam("taskId") int taskId, @PathParam("jobId") int jobId, @HeaderParam("Token") String token){
        User user = userManagementController.getUserByToken(token);
        try {
            jobController.lockJobTask(taskId, jobId, user.getId());
        } catch (JobTaskLockException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", e.getMessage());
            envelope.setError(error);
            return Response.status(403).entity(envelope).build();
        } catch (JobTaskNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not found", e.getMessage());
            envelope.setError(error);
            return Response.status(404).entity(envelope).build();
        }
        return Response.ok().build();
    }

    /**
     * Unlocken eines Jobs eines Task.
     *
     * @param taskId ID des Tasks
     * @param jobId ID des Jobs
     * @param token  session token um sich zu authentifizieren
     * @return
     */
    @DELETE
    @Path("{taskId}/jobs/{jobId}/lock")
    @Produces("application/json")
    public Response unlockJobAtTaskPosition(@PathParam("taskId") int taskId, @PathParam("jobId") int jobId, @HeaderParam("Token") String token){
        User user = userManagementController.getUserByToken(token);
        try {
            jobController.unlockJobTask(taskId, jobId, user.getId());
        } catch (JobTaskLockException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", e.getMessage());
            envelope.setError(error);
            return Response.status(403).entity(envelope).build();
        } catch (JobTaskNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not found", e.getMessage());
            envelope.setError(error);
            return Response.status(404).entity(envelope).build();
        }
        return Response.ok().build();
    }
}
