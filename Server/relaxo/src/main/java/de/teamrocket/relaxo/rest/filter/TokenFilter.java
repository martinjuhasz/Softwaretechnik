package de.teamrocket.relaxo.rest.filter;

import com.google.inject.Inject;
import de.teamrocket.relaxo.controller.UserManagementController;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.ressources.RestRessource;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Implementierung vom Filter CheckToken.
 */
@CheckToken
public class TokenFilter implements ContainerRequestFilter {

    // Vars

    private final UserManagementController userManagementController;

    // Construct

    @Inject
    public TokenFilter(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
    }

    // Methods

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = requestContext.getHeaderString("Token");

        RestEnvelope<Object> envelope = new RestEnvelope<>();
        ErrorResponse error = new ErrorResponse("token_invalid", "Token is invalid.");
        envelope.setError(error);

        if (token != null) {
            User user = userManagementController.getUserByToken(token);
            // error if user has no session
            if (user == null) {
                requestContext.abortWith(Response.status(RestRessource.CODE_401_UNAUTHORIZED).entity(envelope).build());
            }
        } else {
            requestContext.abortWith(Response.status(RestRessource.CODE_401_UNAUTHORIZED).entity(envelope).build());
        }
    }
}
