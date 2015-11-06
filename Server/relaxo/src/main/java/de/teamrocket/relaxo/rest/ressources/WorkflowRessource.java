package de.teamrocket.relaxo.rest.ressources;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.exceptions.WorkflowItemTypeException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowLockException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowStartItemExistException;
import de.teamrocket.relaxo.models.job.Job;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.models.workflow.WorkflowItem;
import de.teamrocket.relaxo.rest.filter.CheckAdmin;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.formgroup.response.FormGroupResponse;
import de.teamrocket.relaxo.rest.models.formgroup.response.FormGroupResponseFactory;
import de.teamrocket.relaxo.rest.models.job.response.JobCreateResponse;
import de.teamrocket.relaxo.rest.models.workflow.request.FormGroupCreateRequest;
import de.teamrocket.relaxo.rest.models.workflow.request.WorkflowCreateRequest;
import de.teamrocket.relaxo.rest.models.workflow.response.CreateWorkflowResponse;
import de.teamrocket.relaxo.rest.models.workflow.response.TaskResponse;
import de.teamrocket.relaxo.rest.models.workflow.response.WorkflowItemDetailsResponse;
import de.teamrocket.relaxo.rest.models.workflow.response.WorkflowResponse;
import de.teamrocket.relaxo.rest.models.workflowitem.request.WorkflowItemRequest;
import de.teamrocket.relaxo.rest.models.workflowitem.response.WorkflowItemResponse;

/**
 * REST-Ressource um Workflows zu verwalten.
 */
@Path("workflows")
@CheckToken
public class WorkflowRessource extends RestRessource {

    // Vars

    /**
     * Instanz der FormGroupResponseFactory
     */
    private final FormGroupResponseFactory formGroupResponseFactory;

    // Methods

    @Inject
    public WorkflowRessource(FormGroupResponseFactory formGroupResponseFactory) {
        this.formGroupResponseFactory = formGroupResponseFactory;
    }

    /**
     * Erstellen eines neuen Workflows
     *
     * @param token Session-Token des Erstellers
     * @return die Response
     */
    @PUT
    @Produces("application/json")
    @CheckAdmin
    public Response createWorkflow(@HeaderParam("Token") String token, WorkflowCreateRequest createRequest) {
        LOGGER.info("PUT-Request auf /workflows");

        User user = userManagementController.getUserByToken(token);
        Workflow workflow = workflowController.createWorkflow(user, createRequest.getName());

        // Antwort erstellen
        CreateWorkflowResponse createWorkflowResponse = new CreateWorkflowResponse(workflow.getName(), workflow.getId());
        RestEnvelope<CreateWorkflowResponse> envelope = new RestEnvelope<>();
        envelope.setData(createWorkflowResponse);

        return Response.ok(envelope).build();
    }

    /**
     * Loeschen des Workflows mit der uebergebnen ID
     *
     * @param workflowId die ID des Workflows der geloescht werden soll
     * @param token      Session-Token des Users der den Workflow loeschen will
     * @return die Response
     */
    @DELETE
    @Path("{id}")
    @Produces("application/json")
    @CheckAdmin
    public Response deleteWorkflow(@PathParam("id") int workflowId, @HeaderParam("Token") String token) {

        LOGGER.info("DELETE-Request auf /workflows/" + workflowId);

        RestEnvelope envelope = new RestEnvelope();
        Workflow workflow = workflowController.getWorkflowById(workflowId);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            LOGGER.log(Level.INFO, "Delete attempt on not existing Workflow!");
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        /**
         * hat der Workflow noch Jobs?
         */
        if (workflowController.hasActiveJobs(workflow) == true) {
            LOGGER.log(Level.INFO, "Workflow has active Jobs!");
            ErrorResponse error = new ErrorResponse("not_empty", "Workflow has already Jobs.");
            envelope.setError(error);
            return Response.status(CODE_401_UNAUTHORIZED).entity(envelope).build();
        }

        workflowController.deleteWorkflow(workflow);
        return Response.ok().build();

    }

    /**
     * Unlock des Workflows mit der uebergebenen ID
     *
     * @param workflowId die ID des Workflows der unlocked werden soll
     * @param token      Session-Token des Users der den Workflow unlocken will
     * @return die Response
     */
    @POST
    @Path("{id}/lock")
    @Produces("application/json")
    @CheckAdmin
    public Response unlockWorkflow(@PathParam("id") int workflowId, @HeaderParam("Token") String token) {
        LOGGER.info("POST-Request auf /workflows/" + workflowId + "/lock");

        RestEnvelope envelope = new RestEnvelope();
        Workflow workflow = workflowController.getWorkflowById(workflowId);
        User user = userManagementController.getUserByToken(token);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            LOGGER.log(Level.INFO, "Unlock attempt on not existing Workflow!");
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        try {
            workflowController.updateRunnableStateForWorkflow(workflow, true, user);
            return Response.ok().build();
        } catch (WorkflowLockException e) {
            ErrorResponse error = new ErrorResponse("forbidden", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        }

    }

    /**
     * Lock des Workflows mit der uebergebenen ID
     *
     * @param workflowId die ID des Workflows der locked werden soll
     * @param token      Session-Token des Users der den Workflow locken will
     * @return die Response
     */
    @DELETE
    @Path("{id}/lock")
    @Produces("application/json")
    @CheckAdmin
    public Response lockWorkflow(@PathParam("id") int workflowId, @HeaderParam("Token") String token) throws WorkflowLockException {
        LOGGER.info("DELETE-Request auf /workflows/" + workflowId + "/lock");

        RestEnvelope envelope = new RestEnvelope();
        Workflow workflow = workflowController.getWorkflowById(workflowId);
        User user = userManagementController.getUserByToken(token);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            LOGGER.log(Level.INFO, "Lock attempt on not existing Workflow!");
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        workflowController.updateRunnableStateForWorkflow(workflow, false, user);
        return Response.ok().build();
    }

    /**
     * Gibt alle Workflows zurück zu denen der User berechtigt ist.
     *
     * @param token session token um sich zu authentifizieren
     * @return Workflow Liste
     */
    @GET
    @Produces("application/json")
    public Response getWorkflowsForUser(@HeaderParam("Token") String token) {

        LOGGER.info("GET-Request auf /workflows");

        User user = userManagementController.getUserByToken(token);
        RestEnvelope<List<WorkflowResponse>> envelope = new RestEnvelope<>();

        List<Workflow> workflows = workflowController.getWorkflowsForUser(user);
        List<WorkflowResponse> workflowResponses = new LinkedList<>();

        for (Workflow workflow : workflows) {
            WorkflowResponse workflowResponse = new WorkflowResponse(workflow);
            workflowResponse.setUserCanCreateJob(jobController.isUserAbleToStartJobsForWorkflow(workflow, user));
            workflowResponses.add(workflowResponse);
        }
        envelope.setData(workflowResponses);
        return Response.ok(envelope).build();
    }

    /**
     * Gibt alle FormGroups eine Workflows zurück
     *
     * @param workflowId ID des Workflows
     * @param token      session token um sich zu authentifizieren
     * @return FormGroup Liste
     */
    @GET
    @Path("{id}/formgroups")
    @Produces("application/json")
    public Response getFormGroupsForWorkflow(@PathParam("id") int workflowId, @HeaderParam("Token") String token) {

        LOGGER.info("GET-Request auf /workflows/" + workflowId + "/formgroups");

        User user = userManagementController.getUserByToken(token);
        Workflow workflow = workflowController.getWorkflowById(workflowId);

        if (workflow == null) {
            LOGGER.log(Level.INFO, "Workflow not found.");
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        if (workflowController.isUserAbleToSeeWorkflow(workflow, user) == false) {
            LOGGER.log(Level.INFO, "Permission denied!");
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("missing_rights", "You have no rights to view this Workflow");
            envelope.setError(error);
            return Response.status(CODE_401_UNAUTHORIZED).entity(envelope).build();
        }

        List<FormGroup> groups = formGroupController.getFormGroupsForWorkflow(workflow);
        List<FormGroupResponse> groupResponses = new LinkedList<>();
        FormGroupResponse formGroupResponse;
        for (FormGroup formGroup : groups) {
            formGroupResponse = formGroupResponseFactory.create(formGroup);
            groupResponses.add(formGroupResponse);
        }
        RestEnvelope<List<FormGroupResponse>> envelope = new RestEnvelope<>();
        envelope.setData(groupResponses);
        return Response.ok(envelope).build();
    }

    /**
     * Erstellt eine neue FormGruppe für einen Workflow
     *
     * @param workflowId ID des Workflows
     * @param token      session token um sich zu authentifizieren
     * @return  die Response
     */
    @PUT
    @Path("{id}/formgroups")
    @Produces("application/json")
    public Response createFormGroupForWorkflow(FormGroupCreateRequest request, @PathParam("id") int workflowId, @HeaderParam("Token") String token) {

        LOGGER.info("PUT-Request auf /workflows/" + workflowId + "/formgroups");
        Workflow workflow = workflowController.getWorkflowById(workflowId);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            LOGGER.log(Level.INFO, "Delete attempt on not existing Workflow!");
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        String groupName = "Untitled Group";
        if (request != null && request.getName() != null) {
            groupName = request.getName();
        }

        User user = userManagementController.getUserByToken(token);

        FormGroup formGroup = formGroupController.createFormGroup(workflowId, groupName, user);

        RestEnvelope<FormGroupResponse> envelope = new RestEnvelope<>();
        FormGroupResponse response = formGroupResponseFactory.create(formGroup);
        envelope.setData(response);

        return Response.ok(envelope).build();
    }

    /**
     * Erstellt ein neues WorkflowItem
     *
     * @param workflowId          ID des Workflows
     * @param workflowItemRequest Request fuer ein WorkflowItem
     * @param token               Session-Token des Users
     * @return die Response
     */
    @PUT
    @Path("{id}/workflowitems")
    @Produces(MediaType.APPLICATION_JSON)
    @CheckAdmin
    public Response createWorkflowItem(@PathParam("id") int workflowId, WorkflowItemRequest workflowItemRequest, @HeaderParam("Token") String token) {

        LOGGER.info("PUT-Request auf /workflow/" + workflowId + "/workflowitems");
        Workflow workflow = workflowController.getWorkflowById(workflowId);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            RestEnvelope envelope = new RestEnvelope();
            LOGGER.log(Level.INFO, "Delete attempt on not existing Workflow!");
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        WorkflowItem workflowItem;
        try {
            workflowItem = workflowItemController.createWorkflowItem(workflow, workflowItemRequest.getType(), workflowItemRequest.getPosition().getX(), workflowItemRequest.getPosition().getY(), userManagementController.getUserByToken(token));
        } catch (WorkflowStartItemExistException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", "Workflow allready has start item.");
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        } catch (WorkflowItemTypeException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Type does not exist");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        if (workflowItem != null) {
            RestEnvelope<WorkflowItemResponse> envelope = new RestEnvelope<>();
            envelope.setData(new WorkflowItemResponse(workflowItem.getId()));
            return Response.status(CODE_200_OK).entity(envelope).build();
        } else {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("unknown", "An unknown error is occurred.");
            envelope.setError(error);
            return Response.status(CODE_500_INTERNAL_SERVER_ERROR).entity(envelope).build();
        }

    }

    /**
     * Gibt eine Liste mit allen WorkflowItems eines Workflows zurück.
     *
     * @param workflowId die Workflow-Id
     * @param token das UserToken
     * @return die Response
     */
    @GET
    @Path("{id}/workflowitems")
    @Produces(MediaType.APPLICATION_JSON)
    @CheckAdmin
    public Response getWorkflowItems(@PathParam("id") int workflowId, @HeaderParam("Token") String token) {

        LOGGER.info("GET-Request auf /workflow/" + workflowId + "/workflowitems");
        Workflow workflow = workflowController.getWorkflowById(workflowId);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            RestEnvelope envelope = new RestEnvelope();
            LOGGER.log(Level.INFO, "Get attempt on not existing Workflow!");
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        List<WorkflowItem> workflowItems = workflowItemController.getWorkflowItemsByWorkflowId(workflowId);
        List<WorkflowItemDetailsResponse> workflowItemResponses = new LinkedList<>();
        for (WorkflowItem item : workflowItems) {
            workflowItemResponses.add(new WorkflowItemDetailsResponse(item));
        }
        RestEnvelope<List<WorkflowItemDetailsResponse>> envelope = new RestEnvelope<>();
        envelope.setData(workflowItemResponses);
        return Response.ok(envelope).build();
    }


    /**
     * Gibt alle Tasks eine Workflows zurück
     *
     * @param workflowId ID des Workflows
     * @param token      session token um sich zu authentifizieren
     * @return Taskliste
     */

    @GET
    @Path("{id}/tasks")
    @Produces("application/json")
    public Response getTasksForWorkflow(@PathParam("id") int workflowId, @HeaderParam("Token") String token) {

        LOGGER.info("GET-Request auf /workflows/" + workflowId + "/tasks");

        Workflow workflow = workflowController.getWorkflowById(workflowId);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            LOGGER.log(Level.INFO, "Delete attempt on not existing Workflow!");
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        User user = userManagementController.getUserByToken(token);

        if (!workflowController.isUserAbleToSeeWorkflow(workflow, user)) {
            LOGGER.log(Level.INFO, "Permission denied!");
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("missing_rights", "You have no rights to view this Workflow");
            envelope.setError(error);
            return Response.status(CODE_401_UNAUTHORIZED).entity(envelope).build();
        }

        List<Task> tasks = taskController.getTasks(workflow, user);
        List<TaskResponse> taskResponses = new LinkedList<>();
        for (Task task : tasks) {
            taskResponses.add(new TaskResponse(task));
        }

        RestEnvelope<List<TaskResponse>> envelope = new RestEnvelope<>();
        envelope.setData(taskResponses);
        return Response.ok(envelope).build();
    }

    /**
     * Erstellt einen neuen Job für einen Workflow
     *
     * @param workflowId ID des Workflows
     * @param token      session token um sich zu authentifizieren
     * @return 200 bei Erfolg
     */
    @PUT
    @Path("{id}/jobs")
    @Produces("application/json")
    public Response createJobForWorkflow(@PathParam("id") int workflowId, @HeaderParam("Token") String token) {

        LOGGER.info("PUT-Request auf /workflows/" + workflowId + "/jobs");

        Workflow workflow = workflowController.getWorkflowById(workflowId);

        /**
         * gibt es den gewuenschten Workflow?
         */
        if (workflow == null) {
            LOGGER.log(Level.INFO, "Delete attempt on not existing Workflow!");
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Workflow not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        User user = userManagementController.getUserByToken(token);


        // Check, ob User Rechte hat einen neuen Job zu erstellen
        if (!jobController.isUserAbleToStartJobsForWorkflow(workflow, user)) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("missing_rights", "You have no rights to create this Job");
            envelope.setError(error);
            LOGGER.log(Level.WARNING, "USER " + user.getPrename() + " (" + user.getId() + ") tried to create a Job from workflow: " + workflow.getName() + "(" + workflow.getId() + ").");
            return Response.status(CODE_401_UNAUTHORIZED).entity(envelope).build();
        }

        // erstelle neuen Job
        Job job = jobController.createJob(workflow, user);
        if (job == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("unknown", "Ein unbekannter Fehler ist aufgetreten.");
            envelope.setError(error);
            LOGGER.log(Level.WARNING, "Job mit dem Workflow " + workflow.getName() + " (" + workflow.getId() + ") konnte nicht von " + user.getUsername() + " (" + user.getId() + ") erstellt werden.");
            return Response.status(CODE_500_INTERNAL_SERVER_ERROR).entity(envelope).build();
        }

        JobCreateResponse jobCreateResponse = new JobCreateResponse();
        jobCreateResponse.setJobId(job.getId());

        WorkflowItem nextWorkflowItem = jobController.moveNewJobToFirstWorkflowItem(workflow, user, job);


        if (nextWorkflowItem != null) {
            jobCreateResponse.setTaskId(nextWorkflowItem.getId());
        } else {
            jobCreateResponse.setTaskId(null);
        }

        RestEnvelope<JobCreateResponse> envelope = new RestEnvelope<>();
        envelope.setData(jobCreateResponse);
        return Response.ok(envelope).build();
    }
}
