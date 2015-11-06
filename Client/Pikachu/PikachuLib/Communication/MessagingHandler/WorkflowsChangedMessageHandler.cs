using System.Diagnostics;
using Apache.NMS;
using Newtonsoft.Json;
using PikachuLib.Communication.Event;
using PikachuLib.Models.Workflow;

namespace PikachuLib.Communication
{
    /// <summary>
    /// Wird verwendet um den Payload einer Message umzuwandeln.
    /// </summary>
    public class MessagingWorkflowPayload
    {
        [JsonProperty("userId")]
        public int UserId { get; set; }

        [JsonProperty("workflowId")]
        public int WorkflowId { get; set; }
    }

    /// <summary>
    /// Wrapper-Klasse zum Bearbeiten einer eingehenden Message-Nachricht.
    /// </summary>
    public class WorkflowsChangedMessageHandler : MessageHandler
    {

        public WorkflowsChangedMessageHandler(IMessageConsumer consumer, MessagingClient.EventDelegate callback)
            :base(consumer, callback)
        {
        }

        /// <summary>
        /// Methode zum empfangen von Nachrichten.
        /// </summary>
        /// <param name="message"></param>
        protected override void OnMessageReceived(IMessage message)
        {
            // hole UserId von Message
            var m = message as ITextMessage;
            var response = JsonConvert.DeserializeObject<MessagingWorkflowPayload>(m.Text);

            callback(new WorkflowsChangedEventArgs(response.WorkflowId, response.UserId));
        }

    }
}
