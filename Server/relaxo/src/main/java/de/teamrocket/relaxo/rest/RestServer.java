package de.teamrocket.relaxo.rest;

import java.net.URI;
import java.util.logging.Level;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.net.httpserver.HttpServer;

import de.teamrocket.relaxo.rest.filter.AdminFilter;
import de.teamrocket.relaxo.rest.filter.TokenFilter;
import de.teamrocket.relaxo.rest.ressources.FormGroupRessource;
import de.teamrocket.relaxo.rest.ressources.JobRessource;
import de.teamrocket.relaxo.rest.ressources.SessionRessource;
import de.teamrocket.relaxo.rest.ressources.TaskRessource;
import de.teamrocket.relaxo.rest.ressources.UserGroupRessource;
import de.teamrocket.relaxo.rest.ressources.UserRessource;
import de.teamrocket.relaxo.rest.ressources.WorkflowDecisionRessource;
import de.teamrocket.relaxo.rest.ressources.WorkflowItemRessource;
import de.teamrocket.relaxo.rest.ressources.WorkflowRessource;
import de.teamrocket.relaxo.rest.ressources.WorkflowScriptRessource;
import de.teamrocket.relaxo.rest.ressources.WorkflowStartRessource;
import de.teamrocket.relaxo.util.config.Config;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

/**
 * RestServer implementiert die Rest-Schnittstelle des Relaxo Servers
 */
@Singleton
public class RestServer {

    // Static

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);
    private static final int TIMEOUT = 2;
    private static String BASE_URI;

    // Vars
    private final FormGroupRessource formGroupRessource;
    private final JobRessource jobRessource;
    private final SessionRessource sessionRessource;
    private final TaskRessource taskRessource;
    private final WorkflowDecisionRessource workflowDecisionRessource;
    private final WorkflowStartRessource startRessource;
    private final WorkflowItemRessource workflowItemRessource;
    private final WorkflowRessource workflowRessource;
    private final WorkflowScriptRessource workflowScriptRessource;
    private final TokenFilter tokenFilter;
    private final UserGroupRessource userGroupRessource;
    private final UserRessource userRessource;
    private final AdminFilter adminFilter;
    private HttpServer httpServer;

    // Construct
    @Inject
    public RestServer(FormGroupRessource formGroupRessource, JobRessource jobRessource, SessionRessource sessionRessource,
                      TaskRessource taskRessource, WorkflowStartRessource startRessource, WorkflowDecisionRessource workflowDecisionRessource, WorkflowItemRessource workflowItemRessource,
                      WorkflowRessource workflowRessource, WorkflowScriptRessource workflowScriptRessource, TokenFilter tokenFilter, UserGroupRessource userGroupRessource,
                      UserRessource userRessource, AdminFilter adminFilter) {

        this.formGroupRessource = formGroupRessource;
        this.jobRessource = jobRessource;
        this.sessionRessource = sessionRessource;
        this.taskRessource = taskRessource;
        this.startRessource = startRessource;
        this.workflowDecisionRessource = workflowDecisionRessource;
        this.workflowItemRessource = workflowItemRessource;
        this.workflowScriptRessource = workflowScriptRessource;
        this.workflowRessource = workflowRessource;
        this.userGroupRessource = userGroupRessource;
        this.userRessource = userRessource;
        this.tokenFilter = tokenFilter;
        this.adminFilter = adminFilter;

        Config config = Config.getSingleton();//TODO inject!

        RestServer.BASE_URI = "http://" + config.getString("rest.ip") + ":" + config.getString("rest.port") + "/";

    }

    // Methods

    /**
     * Startet den HTTPServer der Rest-Schnittstelle
     */
    public void start() {

        // do nothing if server is already running
        if (httpServer != null) {
            return;
        }

        ResourceConfig rc = new ResourceConfig();
        rc.register(formGroupRessource);
        rc.register(jobRessource);
        rc.register(taskRessource);
        rc.register(startRessource);
        rc.register(sessionRessource);
        rc.register(workflowDecisionRessource);
        rc.register(workflowItemRessource);
        rc.register(workflowRessource);
        rc.register(workflowScriptRessource);
        rc.register(userGroupRessource);
        rc.register(userRessource);
        rc.register(adminFilter, 0);
        rc.register(tokenFilter, 1);
        rc.register(RestExceptionListener.class);

        this.httpServer = JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        LOGGER.log(Level.INFO, "REST-Server startet on: " + BASE_URI);
    }

    /**
     * Stoppt den HTTPServer der Rest-Schnittstelle
     */
    public void stop() {
        LOGGER.log(Level.WARNING, "Server shutdown in " + TIMEOUT + " seconds");
        httpServer.stop(TIMEOUT);
        LOGGER.log(Level.INFO, "Server stopped");
    }
}
