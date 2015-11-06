package de.teamrocket.relaxo.rest.ressources;

import de.teamrocket.relaxo.models.workflow.Start;
import de.teamrocket.relaxo.rest.filter.CheckAdmin;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.start.request.StartDetailRequest;
import de.teamrocket.relaxo.rest.models.start.response.StartDetailResponse;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * REST-Ressource um StartItems zu verwalten.
 */
@Path("workflowstart")
@CheckToken
public class WorkflowStartRessource extends RestRessource {

    // Static

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

    // Methods

    /**
     * Setzte die Usergruppen einer WorkflowStart.
     *
     * @param workflowStartId ID des WorkflowStart
     * @param token session token um sich zu authentifizieren
     * @return 200 bei Erfolg.
     */
    @POST
    @Path("{id}")
    @CheckAdmin
    @Produces("application/json")
    public Response updateWorkflowitemStart(@PathParam("id") int workflowStartId, StartDetailRequest startRequest, @HeaderParam("Token") String token) {

        LOGGER.info("POST-Request auf /workflowstart/{id}");

        Start workflowStart = (Start) workflowItemController.getWorkflowItemById(workflowStartId);
        if (workflowStart == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Start ID not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        workflowItemController.setUserGroupsForWorkflowItem(workflowStart, startRequest.getUsergroups());

        LOGGER.info("Workflowitem mit der id " + workflowStartId + " wurde geupdatet.");

        return Response.ok().build();

    }

    /**
     * Gibt die Usergruppen einer WorkflowStart zurück.
     *
     * @param workflowStartId ID des WorkflowStart
     * @param token           session token um sich zu authentifizieren
     * @return 200 mit Payload bei Erfolg.
     */
    @GET
    @Path("{id}")
    @CheckAdmin
    @Produces("application/json")
    public Response getWorkflowitemStart(@PathParam("id") int workflowStartId, @HeaderParam("Token") String token) {

        LOGGER.info("GET-Request auf /workflowstart/{id}");

        Start workflowStart = (Start) workflowItemController.getWorkflowItemById(workflowStartId);
        if (workflowStart == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "Start ID not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        RestEnvelope<StartDetailResponse> envelope = new RestEnvelope<>();

        StartDetailResponse response = new StartDetailResponse();
        response.setUsergroups(workflowItemController.getUserGroupsForWorkflowItem(workflowStart));
        envelope.setData(response);
        return Response.ok(envelope).build();
    }

}
