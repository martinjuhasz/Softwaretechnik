using System.Diagnostics;
using Apache.NMS;
using Newtonsoft.Json;
using PikachuLib.Communication.Event;

namespace PikachuLib.Communication
{
    /// <summary>
    /// Wird verwendet um den Payload einer Message umzuwandeln.
    /// </summary>
    public class MessagingUserPayload
    {
        [JsonProperty("userId")]
        public int UserId { get; set; }
    }


    /// <summary>
    /// Wrapper-Klasse zum Bearbeiten einer eingehenden Message-Nachricht.
    /// </summary>
    public class WorkflowEditorMessageHandler : WorkflowMessageHandler
    {

        public WorkflowEditorMessageHandler(IMessageConsumer consumer, MessagingClient.EventDelegate callback, int workflowId)
            :base(consumer, callback, workflowId)
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
            var userIdWrapper = JsonConvert.DeserializeObject<MessagingUserPayload>(m.Text);

            callback(new WorkflowMessageEditorEventArgs(WorkflowId, userIdWrapper.UserId));
        }

    }
}
