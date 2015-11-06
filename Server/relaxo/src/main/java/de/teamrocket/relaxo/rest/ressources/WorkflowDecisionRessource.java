package de.teamrocket.relaxo.rest.ressources;

import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowDecision;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.WorkflowDecision.request.TaskVariableRequest;
import de.teamrocket.relaxo.rest.models.WorkflowDecision.request.WorkflowDecisionUpdateRequest;
import de.teamrocket.relaxo.rest.models.WorkflowDecision.response.WorkflowDecisionResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * REST-Ressource um WorkflowDecision zu verwalten.
 */
@Path("workflowdecision")
@CheckToken
public class WorkflowDecisionRessource extends RestRessource {

    // Methods

    /**
     * Gibt Details einer WorkflowDecision zurück.
     *
     * @param workflowDecisionId ID der WorkflowDecision
     * @return WorkflowDecision Details als JSON
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getWorkflowDecision(@PathParam("id") int workflowDecisionId) {
        try {
            WorkflowDecision workflowDecision = workflowDecisionController.getWorkflowDecision(workflowDecisionId);
            WorkflowDecisionResponse workflowDecisionResponse = new WorkflowDecisionResponse(workflowDecision);
            RestEnvelope<WorkflowDecisionResponse> envelope = new RestEnvelope<>();
            envelope.setData(workflowDecisionResponse);

            return Response.ok(envelope).build();
        } catch (WorkflowItemNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }
    }

    /**
     * Update einer WorkflowDecision.
     *
     * @param workflowDecisionId ID der WorkflowDecision
     * @return
     */
    @POST
    @Path("{id}")
    @Produces("application/json")
    public Response updateWorkflowDecision(@PathParam("id") int workflowDecisionId, WorkflowDecisionUpdateRequest workflowDecisionUpdateRequest) {

        String condition = workflowDecisionUpdateRequest.getCondition();
        int nextWorkflowItemOnTrue = workflowDecisionUpdateRequest.getNextWorkflowItemOnTrue();
        List<TaskVariable> variables = new LinkedList<>();
        TaskVariable taskVariable;
        for (TaskVariableRequest taskVariableRequest : workflowDecisionUpdateRequest.getVariables()) {
            taskVariable = new TaskVariable();
            taskVariable.setName(taskVariableRequest.getName());
            taskVariable.setTaskComponentId(taskVariableRequest.getTaskComponentId());
            taskVariable.setTaskId(taskVariableRequest.getTaskId());
            variables.add(taskVariable);
        }

        try {
            workflowDecisionController.updateWorkflowDecision(workflowDecisionId, variables, condition, nextWorkflowItemOnTrue);
        } catch (WorkflowItemNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(404).entity(envelope).build();
        }
        return Response.ok().build();

    }

}
