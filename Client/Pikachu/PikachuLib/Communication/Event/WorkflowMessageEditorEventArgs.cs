

using System;
namespace PikachuLib.Communication.Event
{
    /// <summary>
    /// Wrapper-Klasse zum übergeben des Payloads einer WorkflowEditor-Message
    /// </summary>
    public class WorkflowMessageEditorEventArgs : EventArgs
    {
        public int WorkflowId { get; private set; }
        public int UserId { get; private set; }

        public WorkflowMessageEditorEventArgs(int workflowId, int userId)
        {
            WorkflowId = workflowId;
            UserId = userId;
        }
        
    }
}
