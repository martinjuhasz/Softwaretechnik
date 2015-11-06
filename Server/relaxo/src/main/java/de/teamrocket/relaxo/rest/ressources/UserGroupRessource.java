package de.teamrocket.relaxo.rest.ressources;

import de.teamrocket.relaxo.controller.exceptions.DuplicateException;
import de.teamrocket.relaxo.controller.exceptions.NotNullException;
import de.teamrocket.relaxo.controller.exceptions.UserGroupNotFoundException;
import de.teamrocket.relaxo.controller.exceptions.UserNotFoundException;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.rest.filter.CheckAdmin;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import org.apache.ibatis.exceptions.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * REST-Ressource um Benutzer zu verwalten.
 */
@Path("usergroup")
@CheckAdmin
@CheckToken
public class UserGroupRessource extends RestRessource {

    // Methods

    /**
     * Erstellt eine Nutzergruppe 
     *
     * @param group UserGroupBean welche durch den Request befüllt wird
     * @return Response mit der ID der neu erstellten UserGroup
     */
    @PUT
    @Produces("application/json")
    public Response createUserGroup(UserGroup group) {
        LOGGER.info("PUT-Request auf /usergroup");
        RestEnvelope<UserGroup> envelope = new RestEnvelope<>();

        try {
            userManagementController.createUserGroup(group);
        } catch (NotNullException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("Not Null Constraint", "Es muss ein Name gesetzt werden.");
            envelope.setError(error);
            return Response.status(CODE_400_BAD_REQUEST).entity(envelope).build();
        } catch (DuplicateException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("Duplicate", "Diese UserGroup gibt es bereits");
            envelope.setError(error);
            return Response.status(CODE_400_BAD_REQUEST).entity(envelope).build();
        }

        envelope.setData(group);

        return Response.ok(envelope).build();
    }

    /**
     * Gibt alle Benutzergruppen zurück.
     *
     * @return Liste aller Benutzergruppen als JSON.
     */
    @GET
    @Produces("application/json")
    public Response getAllUserGroups() {
        LOGGER.info("GET-Request auf /usergroup");
        RestEnvelope<List<UserGroup>> envelope = new RestEnvelope<>();
        List<UserGroup> userGroups = userManagementController.getAllGroups();
        envelope.setData(userGroups);
        return Response.ok(envelope).build();
    }

    /**
     * Gibt alle Benutzer einer Gruppe zurück
     *
     * @param groupId id welche angibt um welche Gruppe es sich handelt.
     * @return Liste der Benutzer zu die zur jeweiligen Gruppe gehören
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getUsersByGroup(@PathParam("id") int groupId) {
        List<User> users;
        RestEnvelope<List<User>> envelope = new RestEnvelope<>();
        try {
            users = userManagementController.getUserForUserGroup(groupId);
        } catch (UserGroupNotFoundException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("not_found", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        envelope.setData(users);
        return Response.ok(envelope).build();
    }

    /**
     * Fügt einen Nutzer einer Benutzergruppe hinzu
     *
     * @param userGroupId Id um die Nutzergruppe zu identifizieren
     * @param userId      Id um den Nutzer zu identifizieren
     * @return vermeldet Erfolg/Misserfolg der Operation.
     */
    @POST
    @Path("{userGroupId}/user/{userId}")
    public Response addUserToUsergroup(@PathParam("userGroupId") int userGroupId, @PathParam("userId") int userId) {
        RestEnvelope envelope = new RestEnvelope();
        try {
            userManagementController.addUserToGroup(userId, userGroupId);
        } catch (UserNotFoundException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("not_found", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        } catch (UserGroupNotFoundException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("not_found", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        } catch (PersistenceException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("forbidden", "User mit der id " + Integer.toString(userId) + " ist bereits der " +
                    "Gruppe " + Integer.toString(userGroupId) + " zugeordnet.");
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        }
        return Response.ok().build();
    }

    /**
     * Löscht einen Nutzer aus einer Nutzergruppe
     *
     * @param userGroupId Id um die Nutzergruppe zu identifizieren
     * @param userId      Id um den Nutzer zu identifizieren
     * @return vermeldet Erfolg/Misserfolg der Operation.
     */
    @DELETE
    @Path("{userGroupId}/user/{userId}")
    public Response removeUserFromUserGroup(@PathParam("userGroupId") int userGroupId, @PathParam("userId") int userId) {
        userManagementController.removeUserFromUserGroup(userId, userGroupId);
        return Response.ok().build();
    }

}
