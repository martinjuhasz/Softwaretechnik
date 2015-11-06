using System;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflowitem
{
    /// <summary>
    /// Repräsentation eines Lock WorkflowItem Requests
    /// </summary>
    public class LockWorkflowItemRequest : RequestBase
    {
        public LockWorkflowItemRequest(int workflowItemId)
        {
            RestUrl = String.Format("/workflowitems/{0}/lock", workflowItemId);
            Method = Method.POST;
        }
    }
}
