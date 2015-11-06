using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
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
