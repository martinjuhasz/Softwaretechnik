package de.teamrocket.relaxo.rest;

import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

/**
 * Listener for REST exceptions. Used to log exceptions that
 * are catched by the jersey server.
 */
public class RestExceptionListener implements ApplicationEventListener {

    // Methods

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return new ExceptionRequestEventListener();
    }

    @Override
    public void onEvent(ApplicationEvent arg0) {
        // Nothing to do
    }

    /**
     * Internal class to log exceptions that are thrown while a request is processed.
     */
    private static class ExceptionRequestEventListener implements RequestEventListener {
        private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.REST);

        @Override
        public void onEvent(RequestEvent event) {
            switch (event.getType()) {
                case ON_EXCEPTION:
                    final Throwable exception = event.getException();

                    if (exception.getMessage().startsWith("HTTP 404 Not Found")) {
                        LOGGER.warning(exception.getMessage() + " | on URI: /" + event.getUriInfo().getPath(), null);
                    } else {
                        LOGGER.warning(exception.getMessage(), (Exception)exception);
                    }

                    break;
                default:
                    break;
            }
        }
    }
}