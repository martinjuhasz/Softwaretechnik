using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using RestSharp;

namespace PikachuLib.Communication.Models.Request
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
