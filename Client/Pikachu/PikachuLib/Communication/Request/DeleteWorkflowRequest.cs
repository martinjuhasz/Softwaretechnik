using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;
using Newtonsoft.Json;

namespace PikachuLib.Communication.Models.Request
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
