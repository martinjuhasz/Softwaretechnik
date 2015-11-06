package de.teamrocket.relaxo.rest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.teamrocket.relaxo.RelaxoModule;
import de.teamrocket.relaxo.controller.JobController;
import de.teamrocket.relaxo.controller.UserManagementController;
import de.teamrocket.relaxo.controller.WorkflowController;
import de.teamrocket.relaxo.controller.WorkflowItemController;
import de.teamrocket.relaxo.persistence.services.ServiceModule;
import de.teamrocket.relaxo.util.config.Config;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Superklasse aller Tests für REST-Ressourcen. Instanziiert einen RestServer und einen REST-Client.
 */
public class RessourceTest {

    /**
     * der REST-Server
     */
    protected static RestServer server;
    protected static JobController jobController;
    protected static UserManagementController userManagementController;
    protected static WorkflowController workflowController;
    protected static WorkflowItemController workflowItemController;
    /**
     * Der REST-Client für Serveranfragen
     */
    protected static Client client;
    private static Injector injector;
    /**
     * Configuration
     */
    private static Config config;

    /**
     * Instanziiert alle benötigten Ressourcen zum testen
     */
    @BeforeClass
    public static void testSetUp() {

        injector = Guice.createInjector(new RelaxoModule(), new ServiceModule());
        config = Config.getSingleton();
        config.set("activemq", "false");


        injector = Guice.createInjector(new RelaxoModule(), new ServiceModule());

        jobController = injector.getInstance(JobController.class);
        workflowController = injector.getInstance(WorkflowController.class);
        workflowItemController = injector.getInstance(WorkflowItemController.class);
        userManagementController = injector.getInstance(UserManagementController.class);


        server = injector.getInstance(RestServer.class);
        server.start();

        client = ClientBuilder.newClient();

    }

    /**
     * Stoppt den REST-Server
     */
    @AfterClass
    public static void testTearDown() {
        server.stop();
    }

}
