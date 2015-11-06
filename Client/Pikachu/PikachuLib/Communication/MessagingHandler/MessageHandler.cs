using System.Diagnostics;
using Apache.NMS;
using Newtonsoft.Json;
using PikachuLib.Communication.Event;
using System;

namespace PikachuLib.Communication
{
    /// <summary>
    /// Wrapper-Klasse zum Bearbeiten einer eingehenden Message-Nachricht.
    /// </summary>
    public class MessageHandler
    {
        protected readonly IMessageConsumer consumer;
        protected readonly MessagingClient.EventDelegate callback;

        /// <summary>
        /// Gibt den Consumer der Nachrichten zurück.
        /// </summary>
        public IMessageConsumer Consumer
        {
            get { return consumer; }
        }

        /// <summary>
        /// Gibt die Callback-Methode zurück, die aufgerufen wird, wenn eine Nachricht eingegangen ist.
        /// </summary>
        public MessagingClient.EventDelegate Callback
        {
            get { return callback; }
        }

        public MessageHandler(IMessageConsumer consumer, MessagingClient.EventDelegate callback)
        {
            this.consumer = consumer;
            this.callback = callback;

            AddListener();
        }

        /// <summary>
        /// Methode zum empfangen von Nachrichten.
        /// </summary>
        /// <param name="message"></param>
        protected virtual void OnMessageReceived(IMessage message)
        {
            callback(new EventArgs());
        }

        /// <summary>
        /// Fügt den Listener dem Consumer hinzu.
        /// </summary>
        public void AddListener()
        {
            consumer.Listener += OnMessageReceived;
        }

        /// <summary>
        /// Entfernt den Listner vom Consumer.
        /// </summary>
        public void RemoveListener()
        {
            consumer.Listener -= OnMessageReceived;
        }
    }

}
