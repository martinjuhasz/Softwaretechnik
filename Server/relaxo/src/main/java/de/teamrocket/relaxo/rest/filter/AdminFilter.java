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
 * Implementierung vom Filter CheckAdmin.
 */
@CheckAdmin
public class AdminFilter implements ContainerRequestFilter {

    // Vars

    private final UserManagementController userManagementController;

    // Construct

    @Inject
    public AdminFilter(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
    }

    // Methods

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = requestContext.getHeaderString("Token");

        if (token != null) {
            User user = userManagementController.getUserByToken(token);

            // error if user is no admin
            if (!userManagementController.isUserAdmin(user)) {
                RestEnvelope envelope = new RestEnvelope();
                ErrorResponse error = new ErrorResponse("missing_rights", "You need to be admin");
                envelope.setError(error);
                requestContext.abortWith(Response.status(RestRessource.CODE_401_UNAUTHORIZED).entity(envelope).build());
            }
        }
    }
}


