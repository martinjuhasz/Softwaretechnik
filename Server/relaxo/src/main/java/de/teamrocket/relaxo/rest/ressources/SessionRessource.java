package de.teamrocket.relaxo.rest.ressources;

import de.teamrocket.relaxo.controller.exceptions.UserNotFoundException;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.session.request.SessionRequest;
import de.teamrocket.relaxo.rest.models.session.response.SessionResponse;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * REST-Ressource um Sessions von Usern zu verwalten.
 */
@Path("sessions")
public class SessionRessource extends RestRessource {

    // Static

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

    // Methods

    /**
     * Prüft Logindaten und erstellt ggf. eine Session.
     *
     * @param sessionRequest Payload des Requests
     * @return Token bei Erfolg
     */
    @POST
    @Produces("application/json")
    public Response addSession(SessionRequest sessionRequest) {

        LOGGER.info("POST-Request auf /sessions");
        User user;
        String token;

        try {
            user = userManagementController.getUserWithCredentials(sessionRequest.getUsername(), sessionRequest.getPassword());
        } catch (UserNotFoundException e) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", e.getMessage());
            envelope.setError(error);
            return Response.status(404).entity(envelope).build();
        }

        token = userManagementController.createSessionForUser(user);

        RestEnvelope<SessionResponse> envelope = new RestEnvelope<>();
        SessionResponse sessionResponse = new SessionResponse(token, user);

        if (user.isActive()) {
            envelope.setData(sessionResponse);
        } else {
            envelope.setError(new ErrorResponse("invalid_account", "Dein Account ist deaktiviert."));
        }


        return Response.ok(envelope).build();
    }

    /**
     * Löscht eine Session mittels Token.
     *
     * @param token der Token der zu löschenden Session
     * @return 200 bei Erfolg
     */
    @CheckToken
    @DELETE
    @Produces("application/json")
    public Response deleteSession(@HeaderParam("Token") String token) {

        LOGGER.info("DELETE-Request auf /sessions");

        userManagementController.removeSessionForUser(token);

        return Response.ok().build();

    }
}
