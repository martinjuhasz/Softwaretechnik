package de.teamrocket.relaxo.events.subscriber;

import com.google.common.eventbus.Subscribe;
import de.teamrocket.relaxo.events.models.TaskUpdateEvent;
import de.teamrocket.relaxo.messaging.Publisher;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;

import javax.jms.JMSException;

/**
 * Verarbeitet alle Events die im Zyklus eines Jobdurchlaufs durch einen Workflow entstehen
 */

public class JobSubscriber {

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.EVENT);
    private final Publisher publisher;

    public JobSubscriber(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Verarbeitet Events wenn ein Task aktualisiert wurde
     *
     * @param event das zu verarbeitende Event
     */
    @Subscribe
    public void handleTaskUpdate(TaskUpdateEvent event) {

        LOGGER.info("TaskUpdateEvent: " + event);

        if (event.getTask() != null) {
            try {
                publisher.publishTaskUpdate(event.getTask());
            } catch (JMSException e) {
                LOGGER.warning(e.getMessage(), e);
            }
        }
    }
}
