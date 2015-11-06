package de.teamrocket.relaxo.rest.ressources;

import com.google.inject.Inject;
import de.teamrocket.relaxo.models.formgroup.TaskComponentOrder;
import de.teamrocket.relaxo.models.taskcomponent.FormGroup;
import de.teamrocket.relaxo.models.taskcomponent.TaskComponent;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.rest.filter.CheckAdmin;
import de.teamrocket.relaxo.rest.filter.CheckToken;
import de.teamrocket.relaxo.rest.models.ErrorResponse;
import de.teamrocket.relaxo.rest.models.RestEnvelope;
import de.teamrocket.relaxo.rest.models.formgroup.request.ComponentCreateRequest;
import de.teamrocket.relaxo.rest.models.formgroup.request.ComponentOrderUpdateRequest;
import de.teamrocket.relaxo.rest.models.formgroup.request.ComponentsOrderUpdateRequest;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * REST-Ressource um FormGroups zu verwalten.
 */
@Path("formgroups")
@CheckToken
@CheckAdmin
public class FormGroupRessource extends RestRessource {

    // Static

    /**
     * Instanz des Loggers.
     */
    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

    // Vars

    /**
     * Instanz der ComponentFactory.
     */
    private final ComponentFactory componentFactory;

    // Construct

    @Inject
    public FormGroupRessource(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    // Methods

    /**
     * Erstellt eine TaskComponent
     *
     * @param formGroupId   ID der FormGroup in die die TaskComponent hinzugefügt werden soll
     * @param token         das User-Token
     * @param createRequest das Request Objekt
     * @return 200-OK bei Erfolg
     */
    @PUT
    @Path("{id}/components")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createComponent(@PathParam("id") int formGroupId, @HeaderParam("Token") String token, ComponentCreateRequest createRequest) {

        LOGGER.info("PUT-Request auf /formgroups/{id}/components");

        FormGroup formGroup = formGroupController.getFormGroupById(formGroupId);
        if (formGroup == null) {
            RestEnvelope envelope = new RestEnvelope();
            ErrorResponse error = new ErrorResponse("not_found", "FormGroup ID not found.");
            envelope.setError(error);
            return Response.status(CODE_404_NOT_FOUND).entity(envelope).build();
        }

        User user = userManagementController.getUserByToken(token);

        TaskComponent taskComponent = componentFactory.getTaskComponentFromRequest(createRequest);
        workflowItemController.createTaskComponent(taskComponent, user, formGroup.getWorkflowId());

        return Response.ok().build();

    }

    /**
     * Updated die Position mehrerer Components
     *
     * @param formGroupId   ID der FormGroup in die die TaskComponent hinzugefügt werden soll
     * @param token         das User-Token
     * @param updateRequest das Request Objekt
     * @return 200-OK bei Erfolg
     */
    @POST
    @Path("{id}/componentPosition")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateComponentPosition(@PathParam("id") int formGroupId, @HeaderParam("Token") String token, ComponentsOrderUpdateRequest updateRequest) {

        LOGGER.info("POST-Request auf /formgroups/{id}/componentPosition");

        List<TaskComponentOrder> components = new LinkedList<>();
        for (ComponentOrderUpdateRequest componentRequest : updateRequest.getComponents()) {
            components.add(new TaskComponentOrder(componentRequest.getId(), componentRequest.getOrder()));
        }

        formGroupController.updateComponentsOrder(formGroupId, components, userManagementController.getUserByToken(token));

        return Response.ok().build();

    }

}
