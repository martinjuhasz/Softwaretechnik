using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
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
