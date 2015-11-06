using System;
using PikachuLib.Communication.Models.Request;
using RestSharp;

namespace PikachuLib.Communication.Request.Workflow
{
    /// <summary>
    /// Repräsentation eines Delete Workflow Request
    /// </summary>
    public class DeleteWorkflowRequest : RequestBase
    {
        public DeleteWorkflowRequest(int workflowId)
        {
            RestUrl = String.Format("workflows/{0}", workflowId);
            Method = Method.DELETE;
        }
    }
}
