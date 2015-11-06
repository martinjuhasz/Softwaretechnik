package de.teamrocket.relaxo.rest.ressources;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.models.workflow.TaskVariable;
import de.teamrocket.relaxo.models.workflow.WorkflowScript;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.WorkflowDecision.request.TaskVariableRequest;
import de.teamrocket.relaxo.rest.models.workflowscript.request.WorkflowScriptUpdateRequest;
import de.teamrocket.relaxo.rest.models.workflowscript.response.WorkflowScriptResponse;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

/**
 * REST-Ressource um WorkflowDecision zu verwalten.
 */
@Path("workflowscript")
@CheckToken
public class WorkflowScriptRessource extends RestRessource {

    // Static

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

    // Methods

    /**
     * Gibt Details eines WorkflowScript zurück.
     *
     * @param workflowScriptId ID des WorkflowScript
     * @return WorkflowScript Details als JSON
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getWorkflowScript(@PathParam("id") int workflowScriptId) {
        try {
            WorkflowScript workflowScript = workflowScriptController.getWorkflowScript(workflowScriptId);
            WorkflowScriptResponse workflowScriptResponse = new WorkflowScriptResponse(workflowScript);
            RestEnvelope<WorkflowScriptResponse> envelope = new RestEnvelope<>();
            envelope.setData(workflowScriptResponse);

            return Response.ok(envelope).build();
        } catch (WorkflowItemNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }
    }

    /**
     * Update eines WorkflowScript.
     *
     * @param workflowScriptId ID des WorkflowScript
     * @return WorkflowScript Details als JSON
     */
    @POST
    @Path("{id}")
    @Produces("application/json")
    public Response updateWorkflowScript(@PathParam("id") int workflowScriptId, WorkflowScriptUpdateRequest workflowScriptUpdateRequest) {

        String script = workflowScriptUpdateRequest.getScript();
        List<TaskVariable> variables = new LinkedList<>();
        TaskVariable taskVariable;
        for (TaskVariableRequest taskVariableRequest : workflowScriptUpdateRequest.getVariables()) {
            taskVariable = new TaskVariable();
            taskVariable.setName(taskVariableRequest.getName());
            taskVariable.setTaskComponentId(taskVariableRequest.getTaskComponentId());
            taskVariable.setTaskId(taskVariableRequest.getTaskId());
            variables.add(taskVariable);
        }

        try {
            workflowScriptController.updateWorkflowScript(workflowScriptId, variables, script);
        } catch (WorkflowItemNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(404).entity(envelope).build();
        }

        LOGGER.info("WorkflowScript mit der id " + workflowScriptId + " wurde geupdatet.");

        return Response.ok().build();

    }

}
