using System;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Repräsentation eines Get WorkflowItems für einen Workflow Requests
    /// </summary>
    public class GetWorkflowItemsForWorkflowRequest : RequestBase
    {
        public GetWorkflowItemsForWorkflowRequest(int workflowId)
        {
            RestUrl = String.Format("workflows/{0}/workflowitems", workflowId);
            Method = Method.GET;
        }
    }
}
