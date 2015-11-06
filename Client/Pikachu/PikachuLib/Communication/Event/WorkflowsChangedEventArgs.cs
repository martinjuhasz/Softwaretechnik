
using PikachuLib.Models.Workflow;
using System;
namespace PikachuLib.Communication.Event
{
    /// <summary>
    /// Wrapper-Klasse zum übergeben des Payloads einer "WorkflowsChanged"-Message
    /// </summary>
    public class WorkflowsChangedEventArgs : EventArgs
    {
        public int WorkflowId { get; private set; }
        public int UserId { get; private set; }

        public WorkflowsChangedEventArgs(int workflowId, int userId)
        {
            WorkflowId = workflowId;
            UserId = userId;
        }
        
    }
}
