package de.teamrocket.relaxo.rest.ressources;

import de.teamrocket.relaxo.controller.exceptions.NextWorkflowItemException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemLockException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowItemNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.WorkflowNotEditableException;
import de.teamrocket.relaxo.persistence.exceptions.WorkflowItemConnectionException;
import de.teamrocket.relaxo.rest.filter.CheckAdmin;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.workflowitem.request.WorkflowItemRequest;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST-Ressource um WorkflowsItems zu verwalten.
 */
@Path("workflowitems")
@CheckToken
public class WorkflowItemRessource extends RestRessource {

    // Static

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

    // Methods

    /**
     * Entfernt ein WorkflowItem.
     *
     * @param workflowItemId ID des zu löschende WorkflowItems
     * @param token          session token um sich zu authentifizieren
     * @return ReturnCode
     */
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @CheckAdmin
    public Response deleteWorkflowitem(@PathParam("id") int workflowItemId, @HeaderParam("Token") String token) {

        LOGGER.info("DELETE-Request auf /workflowitems/{id}");

        try {
            workflowItemController.deleteWorkflowItem(workflowItemId, userManagementController.getUserByToken(token));
            return Response.ok().build();
        } catch (WorkflowItemNotFoundException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        } catch (WorkflowNotEditableException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", "Workflow is already active.");
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        }
    }

    /**
     * Setzt die XY eines WorkflowItems
     *
     * @param workflowItemId      ID des zu löschende WorkflowItems
     * @param workflowItemRequest Der Payload.
     * @param token               session token um sich zu authentifizieren
     * @return ReturnCode
     */
    @POST
    @Path("{id}/position")
    @Produces(MediaType.APPLICATION_JSON)
    @CheckAdmin
    public Response setWorkflowitemsPosition(@PathParam("id") int workflowItemId, WorkflowItemRequest workflowItemRequest, @HeaderParam("Token") String token) {

        LOGGER.info("POST-Request auf /workflowitems/{id}/position");

        int newX = workflowItemRequest.getPosition().getX();
        int newY = workflowItemRequest.getPosition().getY();

        try {
            workflowItemController.updateWorkflowItemPosition(workflowItemId, newX, newY, userManagementController.getUserByToken(token));
            return Response.ok().build();
        } catch (WorkflowItemNotFoundException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }
    }


    /**
     * Setzt das nächste WorkflowItem eines WorkflowItems
     *
     * @param workflowItemId ID des WorkflowItems
     * @param token          session token um sich zu authentifizieren
     * @return ReturnCode
     */
    @POST
    @Path("{id}/nextitem")
    @Produces(MediaType.APPLICATION_JSON)
    @CheckAdmin
    public Response setNextWorkflowItem(@PathParam("id") int workflowItemId, @HeaderParam("Token") String token, WorkflowItemRequest workflowItemRequest) {
        LOGGER.info("POST-Request auf /workflowitems/" + workflowItemId + "/nextitem");

        try {
            workflowItemController.setNextWorkflowItems(workflowItemId, workflowItemRequest.getNextItem(), userManagementController.getUserByToken(token));
            return Response.ok().build();
        } catch (NextWorkflowItemException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        } catch (WorkflowItemNotFoundException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not found", "WorkflowItem not found");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        } catch (WorkflowItemConnectionException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not found", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_400_BAD_REQUEST).entity(envelope).build();
		}
    }


    /**
     * Locked ein WorkflowItem.
     *
     * @param workflowItemId ID des WorkflowItems
     * @param token          session token um sich zu authentifizieren
     * @return ReturnCode
     */
    @POST
    @Path("{id}/lock")
    @Produces(MediaType.APPLICATION_JSON)
    @CheckAdmin
    public Response lockWorkflowItem(@PathParam("id") int workflowItemId, @HeaderParam("Token") String token) {
        LOGGER.info("POST-Request auf /workflowitems/{id}/lock");

        try {
            workflowItemController.lockWorkflowItem(workflowItemId, userManagementController.getUserByToken(token));
            return Response.ok().build();
        } catch (WorkflowItemLockException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", "WorkflowItem is already locked.");
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        } catch (WorkflowItemNotFoundException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }


    }

    /**
     * Unlocked ein WorkflowItem.
     *
     * @param workflowItemId ID des WorkflowItems
     * @param token          session token um sich zu authentifizieren
     * @return ReturnCode
     */
    @DELETE
    @Path("{id}/lock")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlockWorkflowItem(@PathParam("id") int workflowItemId, @HeaderParam("Token") String token) {
        LOGGER.info("DELETE-Request auf /workflowitems/"+workflowItemId+"/lock");

        try {
            workflowItemController.unlockWorkflowItem(workflowItemId, userManagementController.getUserByToken(token));
            return Response.ok().build();
        } catch (WorkflowItemLockException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("forbidden", "WorkflowItem is already unlocked.");
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        } catch (WorkflowItemNotFoundException e) {
            LOGGER.warning(e.getMessage());

            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "WorkflowItem not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

    }


}