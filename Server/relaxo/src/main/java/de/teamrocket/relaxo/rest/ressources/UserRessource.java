package de.teamrocket.relaxo.rest.ressources;

import de.teamrocket.relaxo.controller.exceptions.FieldMissingException;
import de.teamrocket.relaxo.controller.exceptions.UserNameTakenException;
import de.teamrocket.relaxo.controller.exceptions.UserNotFoundException;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.usermanagement.UserGroup;
import de.teamrocket.relaxo.rest.filter.CheckAdmin;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.usermanagement.response.CreateUserResponse;
import de.teamrocket.relaxo.rest.models.usermanagement.response.UserResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nakih on 09.01.15.
 */
@CheckToken
@CheckAdmin
@Path("user")
public class UserRessource extends RestRessource {

    // Methods

    /**
     * Gibt Details eines Users zurück.
     *
     * @param userId Id um den User zu identifizieren.
     * @return einen User mit seinen Details als JSON.
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") int userId) {
        User user;
        RestEnvelope<User> envelope = new RestEnvelope<>();
        try {
            user = userManagementController.getUserById(userId);
        } catch (UserNotFoundException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("not_found", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }
        envelope.setData(user);
        return Response.ok(envelope).build();
    }

    /**
     * Gibt alle verfügbaren User zurück.
     *
     * @return Liste aller User als JSON.
     */
    @GET
    @Produces("application/json")
    public Response getUser() {
        List<User> users = userManagementController.getAllUser();
        List<UserResponse> userResponses = new LinkedList<>();

        for (User user : users) {
            userResponses.add(new UserResponse(user));
        }

        RestEnvelope<List<UserResponse>> envelope = new RestEnvelope<>();
        envelope.setData(userResponses);
        return Response.ok(envelope).build();
    }

    /**
     * @param user Bean welche die Userdaten zum erstellen enthält
     * @return Status des Erstellens ( Fehler / Erfolg)
     */
    @PUT
    @Produces("application/json")
    public Response createUser(User user) {
        LOGGER.info("User " + user.toString() + " wird erstellt!");
        RestEnvelope<CreateUserResponse> envelope = new RestEnvelope<>();
        try {
            userManagementController.createUser(user);
        } catch (UserNameTakenException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("username_invalid", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        } catch (FieldMissingException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("missing_field", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        }
        CreateUserResponse createUserResponse = new CreateUserResponse(user.getId());
        envelope.setData(createUserResponse);
        return Response.ok(envelope).build();
    }

    /**
     * Methode zum bearbeiten eines Nutzers.
     * beinhaltet sowohl Änderungen zu Admin oder in-/aktiv setzen des Accounts
     *
     * @param userId ID um zu identifizieren welcher Nutzer geupdated wird
     * @param user   Bean welche die Userdaten zum updaten enthält
     * @return die Response
     */
    @POST
    @Path("{id}")
    @Produces("application/json")
    public Response updateUser(@PathParam("id") int userId, User user) {
        user.setId(userId);
        RestEnvelope<User> envelope = new RestEnvelope<>();
        try {
            userManagementController.updateUser(user);
        } catch (UserNotFoundException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("not_found", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        } catch (FieldMissingException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("missing_field", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_403_FORBIDDEN).entity(envelope).build();
        }
        envelope.setData(user);
        return Response.ok(envelope).build();
    }

    /**
     * Methode um herauszufinden welchen Usergruppen ein Nutzer zugeordnet ist
     *
     * @param id Id des Benutzers
     * @return die Response
     */
    @GET
    @Path("{id}/usergroup")
    public Response getUsergroupsForUser(@PathParam("id") int id) {
        List<UserGroup> usergroups;

        RestEnvelope<List<UserGroup>> envelope = new RestEnvelope<>();


        try {
            usergroups = userManagementController.getUserGroupsForUser(id);
        } catch (UserNotFoundException e) {
            LOGGER.warning(e.getMessage());

            ErrorResponse error = new ErrorResponse("not_found", e.getMessage());
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }


        envelope.setData(usergroups);
        return Response.ok(envelope).build();
    }
}
