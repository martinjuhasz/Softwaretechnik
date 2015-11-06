using Apache.NMS;
using Apache.NMS.ActiveMQ;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using PikachuLib.Communication.Event;

namespace PikachuLib.Communication
{
    /// <summary>
    /// Klasse zum Verbinden und Handeln von Messaging.
    /// </summary>
    public class MessagingClient
    {
        #region properties

        public delegate void EventDelegate(EventArgs message);

        private IConnection connection;
        private ISession session;

        private readonly Dictionary<string, IMessageConsumer> workflowConsumers;
        private readonly List<MessageHandler> workflowHandler;

        #endregion

        #region constructor

        public MessagingClient()
        {
            workflowConsumers = new Dictionary<string, IMessageConsumer>();
            workflowHandler = new List<MessageHandler>();
        }

        #endregion

        #region methods

        /// <summary>
        /// Gibt True zrück, wenn die Verbindung offen ist.
        /// </summary>
        public bool IsConnected {
            get { return connection != null && connection.IsStarted; }
        }

        /// <summary>
        /// Verbindung zu einem ActiveMQ-Server aufbauen.
        /// </summary>
        /// <param name="url">Url-Angabe zu dem Server.</param>
        public void Connect(string url) 
        {
            try
            {
                var connecturi = new Uri(url);

                Debug.Print("MessageClient About to connect to " + connecturi);
                IConnectionFactory factory = new ConnectionFactory(connecturi);

                connection = factory.CreateConnection();
                session = connection.CreateSession();

                connection.Start();
                Debug.Print("MessageClient Connected Successful");
                workflowConsumers.Clear(); // alle consumer löschen!
            }
            catch (Exception ex)
            {
                Debug.Print(ex.Message);
                throw;
            }
        }


        /// <summary>
        /// Fügt einen Consumer hinzu.
        /// </summary>
        /// <param name="topic">Das Messaging Topic.</param>
        /// <param name="callback">Die Callback-Methode, die aufgerufen werden soll, wenn eine Nachricht eingeht.</param>
        public IMessageConsumer createConsumer(string topic)
        {
            if (connection == null)
            {
                throw new Exception("Es wurde noch keine Verbindung zum Server aufgebaut!");
            }

            // Wenn ein Consumer schon für einen Workflow existiert, dann wird dieser genommen,
            // ansonsten wird ein neuer erstellt.
            IMessageConsumer consumer;
            if (workflowConsumers.ContainsKey(topic))
            {
                consumer = workflowConsumers[topic];
            }
            else
            {
                string url = String.Format("topic://{0}", topic);

                Debug.Print("Erstelle einen Consumer für: {0}", url);

                IDestination destination = session.GetDestination(url);
                consumer = session.CreateConsumer(destination);
                workflowConsumers.Add(topic, consumer);
            }

            return consumer;
        }

        /*
        /// <summary>
        /// Entfernt einen Consumer für einen Workflow, wenn dieser exisitiert.
        /// </summary>
        /// <param name="workflowId">Die Workflow-ID</param>
        /// <param name="callback">Die Addresse, die aufgerufen wird, wenn eine Nachricht eingegangen ist.</param>
        public void RemoveWorkflowConsumer(int workflowId, EventDelegate callback)
        {
            var handler = workflowHandler.Find(h => h.WorkflowId == workflowId && h.Callback == callback);
            if (handler != null)
            {
                handler.RemoveListener();
            }
        }
        */

        /// <summary>
        /// Schließt die Verbindung zum Server.
        /// </summary>
        public void Disconnect()
        {
            if (connection == null) return;
            connection.Close();
            connection = null;

            Debug.Print("MessageClient Connection Closed");
        }

        #endregion

        #region Consumers

        /// <summary>
        /// Fügt einen Consumer für einen Workflow hinzu.
        /// </summary>
        /// <param name="workflowId">Die Workflow ID.</param>
        /// <param name="callback">Die Callback-Methode, die aufgerufen werden soll, wenn eine Nachricht eingeht.</param>
        public void AddWorkflowClientConsumer(int workflowId, EventDelegate callback)
        {
            string topic = String.Format("workflow/{0}", workflowId);
            IMessageConsumer consumer = createConsumer(topic);

            var handler = new WorkflowMessageHandler(consumer, callback, workflowId);
            workflowHandler.Add(handler);
        }

        /// <summary>
        /// Fügt einen Consumer für einen Workflow hinzu.
        /// </summary>
        /// <param name="workflowId">Die Workflow ID.</param>
        /// <param name="callback">Die Callback-Methode, die aufgerufen werden soll, wenn eine Nachricht eingeht.</param>
        public void AddWorkflowContentEditorConsumer(int workflowId, EventDelegate callback)
        {
            string topic = String.Format("editor/contenteditor/workflow/{0}", workflowId);
            IMessageConsumer consumer = createConsumer(topic);

            var handler = new WorkflowEditorMessageHandler(consumer, callback, workflowId);
            workflowHandler.Add(handler);
        }

        /// <summary>
        /// Fügt einen Consumer für einen Workflow innerhalb des WorkflowEditors hinzu.
        /// </summary>
        /// <param name="workflowId">Die Workflow ID.</param>
        /// <param name="callback">Die Callback-Methode, die aufgerufen werden soll, wenn eine Nachricht eingeht.</param>
        public void AddWorkflowEditorConsumer(int workflowId, EventDelegate callback)
        {
            string topic = String.Format("editor/workfloweditor/workflow/{0}", workflowId);
            IMessageConsumer consumer = createConsumer(topic);

            var handler = new WorkflowEditorMessageHandler(consumer, callback, workflowId);
            workflowHandler.Add(handler);
        }

        /// <summary>
        /// Fügt einen Consumer für Workflow Änderungen hinzu.
        /// </summary>
        /// <param name="callback">Die Callback-Methode, die aufgerufen werden soll, wenn eine Nachricht eingeht.</param>
        public void AddWorkflowsChangedConsumer(EventDelegate callback)
        {
            string topic = String.Format("editor/workfloweditor/workflow");
            IMessageConsumer consumer = createConsumer(topic);

            var handler = new WorkflowsChangedMessageHandler(consumer, callback);
            workflowHandler.Add(handler);
        }

        /// <summary>
        /// Fügt einen Consumer für den UserEditor hinzu
        /// </summary>
        /// <param name="callback">Die Callback-Methode, die aufgerufen werden soll, wenn eine Nachricht eingeht.</param>
        public void AddUserEditorConsumer(EventDelegate callback)
        {
            string topic = String.Format("editor/usereditor/users");
            IMessageConsumer consumer = createConsumer(topic);

            var handler = new MessageHandler(consumer, callback);
            workflowHandler.Add(handler);
        }

        #endregion
    }
}
