package de.teamrocket.relaxo.events.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.eventbus.Subscribe;
import de.teamrocket.relaxo.events.models.FormGroupsUpdateEvent;
import de.teamrocket.relaxo.events.models.UserUserGroupEvent;
import de.teamrocket.relaxo.events.models.WorkflowUpdateEvent;
import de.teamrocket.relaxo.events.models.WorkflowsUpdateEvent;
import de.teamrocket.relaxo.messaging.Publisher;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import javax.jms.JMSException;

/**
 * Verarbeitet alle Events die relevant für den WorkflowContentEditor sind und wandelt diese in Messages um
 */
public class WorkflowManagerSubscriber {

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.EVENT);

    private final Publisher publisher;

    public WorkflowManagerSubscriber(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Verabreitet Events wenn die FormGroups eines Workflow aktualisiert wurden
     *
     * @param event das zu verarbeitende Event
     */
    @Subscribe
    public void handleFormGroupUpdateEvent(FormGroupsUpdateEvent event) {

        LOGGER.info("FormGroupsUpdateEvent: " + event);

        try {
            publisher.publishFormGroupsUpdate(event.getWorkflowId(), event.getUser());
        } catch (JMSException e) {
            LOGGER.warning(e.getMessage(), e);
        } catch (JsonProcessingException e) {
            LOGGER.warning(e.getMessage(), e);
        }
    }

    /**
     * Verabreitet Events wenn ein Workflow sich geändert hat
     *
     * @param event das Event
     */
    @Subscribe
    public void handleWorkflowUpdateEvent(WorkflowUpdateEvent event) {

        LOGGER.info("WorkflowUpdateEvent: " + event);

        if (event.getWorkflow() != null) {
            try {
                publisher.publishWorkflowUpdate(event.getWorkflow(), event.getUser());
            } catch (JMSException e) {
                LOGGER.warning(e.getMessage(), e);
            } catch (JsonProcessingException e) {
                LOGGER.warning(e.getMessage(), e);
            }
        }
    }

    /**
     * Verarbeitet Events wenn ein Workflow hinzugefügt/entfernt wurde
     *
     * @param event das Event
     */
    @Subscribe
    public void handleWorkflowsUpdateEvent(WorkflowsUpdateEvent event) {

        LOGGER.info("WorkflowsUpdateEvent: " + event);

        try {
            publisher.publishWorkflowsUpdate(event.getWorkflow(), event.getUser());
        } catch (JMSException e) {
            LOGGER.warning(e.getMessage(), e);
        } catch (JsonProcessingException e) {
            LOGGER.warning(e.getMessage(), e);
        }
    }

    /**
     * Verarbeitet Events wenn User/ Usergroups verändert wurden
     *
     * @param event das Event
     */

    @Subscribe
    public void handleUserUserGroupUpdateEvent(UserUserGroupEvent event){
        LOGGER.info("User/UserGroupUpdateEvent: " + event);

        try {
            publisher.publishUserUserGroupUpdate();
        } catch (JMSException e) {
            LOGGER.warning(e.getMessage(), e);
        }
    }
}
