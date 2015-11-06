using System;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflowitem
{
    /// <summary>
    /// Repräsentation eines Delete WorkflowItem Request
    /// </summary>
    public class DeleteWorkflowItemRequest : RequestBase
    {
        public DeleteWorkflowItemRequest(int workflowItemId)
        {
            RestUrl = String.Format("workflowitems/{0}", workflowItemId);
            Method = Method.DELETE;
        }
    }
}
