package de.teamrocket.relaxo.rest.ressources;

import com.google.inject.Inject;

import de.teamrocket.relaxo.controller.*;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

/**
 * Oberklasse aller REST-Ressroucen Klassen. Enthält eine Referenz auf die Datenwelt des Servers
 */
public class RestRessource {

    // Static

    public static final int CODE_404_NOT_FOUND = 404;
    public static final int CODE_403_FORBIDDEN = 403;
    public static final int CODE_401_UNAUTHORIZED  = 401;
    public static final int CODE_400_BAD_REQUEST = 400;
    public static final int CODE_500_INTERNAL_SERVER_ERROR = 500;
    public static final int CODE_501_NOT_IMPLEMENTED = 501;
    public static final int CODE_200_OK = 200;
    public static final int CODE_201_CREATE = 201;
    public static final int CODE_202_ACCEPTED = 202;
    public static final int CODE_204_NO_CONTENT = 204;

    /**
     * Instanz des Loggers.
     */
    protected static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

    // Vars

    @Inject
    protected WorkflowController workflowController;
    @Inject
    protected UserManagementController userManagementController;
    @Inject
    protected JobController jobController;
    @Inject
    protected FormGroupController formGroupController;
    @Inject
    protected TaskController taskController;
    @Inject
    protected WorkflowDecisionController workflowDecisionController;
    @Inject
    protected WorkflowItemController workflowItemController;
    @Inject
    protected WorkflowScriptController workflowScriptController;

}
