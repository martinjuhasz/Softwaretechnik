package de.teamrocket.relaxo.messaging;

import com.google.inject.Singleton;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;


/**
 * Der Broker stellt einen MessageBroker für ActiveMQ zu Verfügung
 */
@Singleton
public class Broker {

    // STATIC

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.DATABASE);

    private static final String PORT = "9999";
    private static final String IP = "0.0.0.0";
    private static final String PROTOCOL = "tcp";

    // VARS

    private BrokerService theBroker;

    // CONSTRUCT

    public Broker() {
        try {
            theBroker = BrokerFactory.createBroker(new URI("properties:broker-config.properties"));
            theBroker.addConnector(PROTOCOL+"://"+IP+":" + PORT);
        } catch (Exception e) {
            LOGGER.warning(e.getMessage(), e);
        }
    }

    // METHODS

    /**
     * Startet den Message-Broker
     */
    public void start() {
        try {
            this.theBroker.start();
        } catch (Exception e) {
            LOGGER.warning(e.getMessage(), e);
        }
    }

    /**
     * Stoppt den Message Broker
     */
    public void stop() {
        try {
            this.theBroker.stop();
        } catch (Exception e) {
            LOGGER.warning(e.getMessage(), e);
        }
    }

}
