

using System;
namespace PikachuLib.Communication.Event
{
    /// <summary>
    /// Wrapper-Klasse zum übergeben des Payloads einer Workflow-Message
    /// </summary>
    public class WorkflowMessageEventArgs : EventArgs
    {
        public int WorkflowId { get; private set; }

        public WorkflowMessageEventArgs(int workflowId)
        {
            WorkflowId = workflowId;
        }
        
    }
}
