using System.Diagnostics;
using Apache.NMS;
using Newtonsoft.Json;
using PikachuLib.Communication.Event;

namespace PikachuLib.Communication
{
    /// <summary>
    /// Wrapper-Klasse zum Bearbeiten einer eingehenden Message-Nachricht.
    /// </summary>
    public class WorkflowMessageHandler : MessageHandler
    {
        private readonly int workflowId;

        /// <summary>
        /// Gibt die Workflow-ID zurück.
        /// </summary>
        public int WorkflowId
        {
            get { return workflowId; }
        }

        public WorkflowMessageHandler(IMessageConsumer consumer, MessagingClient.EventDelegate callback, int workflowId)
            :base(consumer, callback)
        {
            this.workflowId = workflowId;
        }

        /// <summary>
        /// Methode zum empfangen von Nachrichten.
        /// </summary>
        /// <param name="message"></param>
        protected override void OnMessageReceived(IMessage message)
        {
            callback(new WorkflowMessageEventArgs(workflowId));
        }
    }
}
