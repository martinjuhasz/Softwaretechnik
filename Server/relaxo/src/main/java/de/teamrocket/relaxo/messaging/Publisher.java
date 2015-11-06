package de.teamrocket.relaxo.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.teamrocket.relaxo.messaging.beans.MessagingUserBean;
import de.teamrocket.relaxo.messaging.beans.MessagingWorkflowsUpdateBean;
import de.teamrocket.relaxo.models.usermanagement.User;
import de.teamrocket.relaxo.models.workflow.Task;
import de.teamrocket.relaxo.models.workflow.Workflow;
import de.teamrocket.relaxo.util.config.Config;
import de.teamrocket.relaxo.util.logger.RelaxoLogger;
import de.teamrocket.relaxo.util.logger.RelaxoLoggerType;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by mweil002 on 02.12.14.
 */
public class Publisher {

    // STATIC

    private static final RelaxoLogger LOGGER = new RelaxoLogger(RelaxoLoggerType.EVENT);
    private static final String IP = "0.0.0.0";
    private static final String PORT = "9999";
    private static final String PROTOCOL = "tcp";

    // VARS

    private Config config = Config.getSingleton();
    private Connection connection;

    public Publisher() {
    }

    // METHODS

    /**
     * Publiziert eine TaskUpdate Message
     *
     * @param task der Task der geupdated wurde
     * @throws JMSException
     */
    public void publishTaskUpdate(Task task) throws JMSException {
        String topic = String.format("workflow/%d", task.getWorkflowId());
        LOGGER.info("new Topic Message: " + topic);
        submitTopic(topic, "");


    }

    /**
     *Publiziert eine FormGroupsUpdate Message
     *
     * @param workflowId die Workflow-ID
     * @param user der User
     * @throws JMSException
     * @throws JsonProcessingException
     */
    public void publishFormGroupsUpdate(int workflowId, User user) throws JMSException, JsonProcessingException {
        String topic = String.format("editor/contenteditor/workflow/%d", workflowId);
        LOGGER.info("new Topic Message: " + topic);

        ObjectMapper mapper = new ObjectMapper();
        submitTopic(topic, mapper.writeValueAsString(new MessagingUserBean(user)));
    }

    /**
     * Publiziert eine WorkflowUpdate Message
     *
     * @param workflow das Workflow
     * @param user der User
     * @throws JMSException
     * @throws JsonProcessingException
     */
    public void publishWorkflowUpdate(Workflow workflow, User user) throws JMSException, JsonProcessingException {
        String topic = String.format("editor/workfloweditor/workflow/%d", workflow.getId());
        LOGGER.info("new Topic Message: " + topic);

        ObjectMapper mapper = new ObjectMapper();
        submitTopic(topic, mapper.writeValueAsString(new MessagingUserBean(user)));

    }

    /**
     * Erstellt ein Topic welches Änderungen an den Workflows mitteilt (hinzufügen/löschen)
     *
     * @param workflow der neue Workflow
     * @param user     der User welcher die Änderungen gemacht hat
     * @throws JMSException
     * @throws JsonProcessingException
     */
    public void publishWorkflowsUpdate(Workflow workflow, User user) throws JMSException, JsonProcessingException {
        String topic = String.format("editor/workfloweditor/workflow");
        LOGGER.info("new Topic Message: " + topic);

        ObjectMapper mapper = new ObjectMapper();
        String messageContent = mapper.writeValueAsString(new MessagingWorkflowsUpdateBean(workflow.getId(), user.getId()));
        submitTopic(topic, messageContent);

    }

    /**
     * Publiziert ein UserGroupUpdate Message
     * @throws JMSException
     */
    public void publishUserUserGroupUpdate() throws JMSException {
        String topic = "editor/usereditor/users";
        submitTopic(topic,null);
    }

    /**
     * Sendet ein Topic an den Messaging Server
     *
     * @param topic das Topic
     * @param payload der Payload der Message
     * @throws JMSException
     */
    public void submitTopic(String topic, String payload) throws JMSException {
        if (!config.getBool("activemq")){
            return;
        }

        //Create Connection
        if (connection == null) {
            setupConnection();
        }

        //Create Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //Create destination
        Destination destination = session.createTopic(topic);

        //Create Producer on this topic
        MessageProducer producer = session.createProducer(destination);

        //Create a message
        TextMessage message = session.createTextMessage(payload);

        //tell producer to actually send it
        producer.send(message);

        //clean up
        session.close();

    }

    /**
     * Erstellt die Verbindung zum Messaging Server
     * @throws JMSException
     */
    private void setupConnection() throws JMSException {
        //Create ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(PROTOCOL+"://"+IP+":"+PORT);
        connection = connectionFactory.createConnection();
    }
}
