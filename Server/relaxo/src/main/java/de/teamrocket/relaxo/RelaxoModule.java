package de.teamrocket.relaxo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.teamrocket.relaxo.controller.CommandExecutor;
import de.teamrocket.relaxo.controller.FormGroupController;
import de.teamrocket.relaxo.controller.JobController;
import de.teamrocket.relaxo.controller.TaskController;
import de.teamrocket.relaxo.controller.UserManagementController;
import de.teamrocket.relaxo.controller.WorkflowController;
import de.teamrocket.relaxo.controller.WorkflowDecisionController;
import de.teamrocket.relaxo.controller.WorkflowItemController;
import de.teamrocket.relaxo.controller.WorkflowScriptController;
import de.teamrocket.relaxo.controller.commands.WorkflowItemCommandFactory;
import de.teamrocket.relaxo.controller.implementation.FormGroupControllerImplement;
import de.teamrocket.relaxo.controller.implementation.JobControllerImplement;
import de.teamrocket.relaxo.controller.implementation.TaskControllerImplement;
import de.teamrocket.relaxo.controller.implementation.UserManagementControllerImplement;
import de.teamrocket.relaxo.controller.implementation.WorkflowControllerImplement;
import de.teamrocket.relaxo.controller.implementation.WorkflowDecisionControllerImplement;
import de.teamrocket.relaxo.controller.implementation.WorkflowItemControllerImplement;
import de.teamrocket.relaxo.controller.implementation.WorkflowScriptControllerImplement;
import de.teamrocket.relaxo.events.subscriber.JobSubscriber;
import de.teamrocket.relaxo.events.subscriber.WorkflowManagerSubscriber;
import de.teamrocket.relaxo.messaging.Broker;
import de.teamrocket.relaxo.messaging.Publisher;
import de.teamrocket.relaxo.models.job.jobtaskcomponent.JobTaskComponentUpdateVisitorFactory;
import de.teamrocket.relaxo.rest.RestServer;
import de.teamrocket.relaxo.rest.filter.AdminFilter;
import de.teamrocket.relaxo.rest.filter.TokenFilter;
import de.teamrocket.relaxo.rest.models.formgroup.response.FormGroupResponseFactory;
import de.teamrocket.relaxo.rest.models.task.response.TaskComponentResponseFactory;
import de.teamrocket.relaxo.rest.models.task.response.TaskDetailResponseFactory;
import de.teamrocket.relaxo.rest.ressources.ComponentFactory;
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
import de.teamrocket.relaxo.util.ThreadSafePythonInterpreter;
import de.teamrocket.relaxo.util.keyboardinterface.KeyboardInterface;


/**
 *
 */
public class RelaxoModule extends AbstractModule {

    // Vars


    /**
     *
     */
    private Publisher publisher = new Publisher();

    // Methods

    @Override
    protected void configure() {

        binder().requireExplicitBindings();

        // Interface to Implementation
        bind(UserManagementController.class).to(UserManagementControllerImplement.class);
        bind(WorkflowController.class).to(WorkflowControllerImplement.class);
        bind(JobController.class).to(JobControllerImplement.class);
        bind(WorkflowItemController.class).to(WorkflowItemControllerImplement.class);
        bind(FormGroupController.class).to(FormGroupControllerImplement.class);
        bind(TaskController.class).to(TaskControllerImplement.class);
        bind(WorkflowDecisionController.class).to(WorkflowDecisionControllerImplement.class);
        bind(WorkflowScriptController.class).to(WorkflowScriptControllerImplement.class);
        // Add
        bind(Broker.class);
        bind(RestServer.class);
        bind(KeyboardInterface.class);

        // Ressource
        bind(FormGroupRessource.class);
        bind(JobRessource.class);
        bind(SessionRessource.class);
        bind(TaskRessource.class);
        bind(WorkflowDecisionRessource.class);
        bind(WorkflowStartRessource.class);
        bind(WorkflowItemRessource.class);
        bind(WorkflowRessource.class);
        bind(WorkflowScriptRessource.class);
        bind(UserRessource.class);
        bind(UserGroupRessource.class);

        // Filter
        bind(TokenFilter.class);
        bind(AdminFilter.class);

        bind(CommandExecutor.class);
        bind(JobTaskComponentUpdateVisitorFactory.class);
        bind(WorkflowItemCommandFactory.class);
        bind(ComponentFactory.class);
        bind(TaskDetailResponseFactory.class);
        bind(FormGroupResponseFactory.class);
        bind(TaskComponentResponseFactory.class);

        bind(ThreadSafePythonInterpreter.class);

        bind(Publisher.class).toInstance(publisher);

    }

    /**
     *
     * @return
     */
    @Provides
    DateTimeFormatter providesDateTimeFormatter() {
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    }
    
    @Provides @Named("CommandExecutorService")
    ExecutorService provideExecutorService(){
    	return Executors.newSingleThreadExecutor();
    }

    /**
     *
     * @return
     */
    @Provides @Singleton // NOSONAR - Fälschlicherweise als unused markiert.
    private EventBus provideEventBus() {
        EventBus eventBus = new EventBus("RelaxoEventBus");
        eventBus.register(new JobSubscriber(publisher));
        eventBus.register(new WorkflowManagerSubscriber(publisher));
        return eventBus;
    }
}
